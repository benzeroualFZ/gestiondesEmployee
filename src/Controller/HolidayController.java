package Controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

import Model.Holiday;
import Model.HolidayModel;
import View.HolidayView;

public class HolidayController {
    private HolidayModel modelH;
    private HolidayView viewH;

    public HolidayController(HolidayModel modelH, HolidayView viewH) {
        this.modelH = modelH;
        this.viewH = viewH;

        // Attach action listeners
        viewH.getAddBut().addActionListener(e -> addHoliday());
        viewH.getDeleteBut().addActionListener(e -> deleteHoliday());
        viewH.getUpdateBut().addActionListener(e -> modifyHoliday());
    }

 // Utility method to convert Date to LocalDate
    public LocalDate convertToLocalDate(Date date) {
        if (date instanceof java.sql.Date) {
            return ((java.sql.Date) date).toLocalDate();  // Direct conversion from java.sql.Date to LocalDate
        } else {
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();  // Conversion for other Date types
        }
    }

 // Utility method to convert LocalDate to Date
    public Date convertToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    // Method to add a holiday
    public void addHoliday() {
        try {
            String startDateStr = viewH.getDDebut().getText();
            String endDateStr = viewH.getDFin().getText();
            String typeStr = viewH.gettype();
            String employeeStr = viewH.getEmployee();

            // Parse and validate dates
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDate = LocalDate.parse(startDateStr, formatter);
            LocalDate endDate = LocalDate.parse(endDateStr, formatter);

            if (endDate.isBefore(startDate)) {
                viewH.showMessage("La date de fin ne peut pas être antérieure à la date de début.");
                return;
            }

            long daysRequested = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;

            // Retrieve employee ID and check holiday balance
            int empId = modelH.getEmployeeId(employeeStr);
            double availableBalance = modelH.getSoldeDisponible(empId);

            if (daysRequested > availableBalance) {
                viewH.showMessage("Solde insuffisant pour les congés demandés.");
                return;
            }

            // Validate holiday type
            Holiday.Type type;
            try {
                type = Holiday.Type.valueOf(typeStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                viewH.showMessage("Type de congé invalide.");
                return;
            }

            // Create and save the holiday
            Holiday holiday = new Holiday(convertToDate(startDate), convertToDate(endDate), type, empId);
            modelH.addHoliday(holiday);

            viewH.refreshTable();

        } catch (DateTimeParseException e) {
            viewH.showMessage("Format de date invalide. Utilisez yyyy-MM-dd.");
        } catch (Exception e) {
            e.printStackTrace();
            viewH.showMessage("Erreur lors de l'ajout du congé.");
        }
    }

    // Method to modify a holiday
    public void modifyHoliday() {
        try {
            int holidayId = viewH.getSelectedHolidayId();

            if (holidayId == -1) {
                viewH.showMessage("Veuillez sélectionner un congé à modifier.");
                return;
            }

            // Récupération des données saisies
            String startDateStr = viewH.getDDebut().getText();
            String endDateStr = viewH.getDFin().getText();
            String typeStr = viewH.gettype();
            String employeeStr = viewH.getEmployee();

            // Conversion des dates
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDate = LocalDate.parse(startDateStr, formatter);
            LocalDate endDate = LocalDate.parse(endDateStr, formatter);

            if (endDate.isBefore(startDate)) {
                viewH.showMessage("La date de fin ne peut pas être antérieure à la date de début.");
                return;
            }

            long daysRequested = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;

            // Vérification de l'employé et récupération du solde
            int empId = modelH.getEmployeeId(employeeStr);
            double availableBalance = modelH.getSoldeDisponible(empId);

            if (daysRequested > availableBalance) {
                viewH.showMessage("Solde insuffisant pour les congés demandés.");
                return;
            }

            // Validation du type de congé
            Holiday.Type type;
            try {
                type = Holiday.Type.valueOf(typeStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                viewH.showMessage("Type de congé invalide.");
                return;
            }

            // Récupération du congé existant
            Holiday existingHoliday = modelH.getHolidayById(holidayId);

            if (existingHoliday == null) {
                viewH.showMessage("Le congé sélectionné n'existe pas.");
                return;
            }

            // Vérification si les dates ont changé
            if (!existingHoliday.getDDebut().equals(convertToDate(startDate)) || 
                !existingHoliday.getDFin().equals(convertToDate(endDate))) {
                
                // Calcul du nouveau solde
                long oldDaysRequested = java.time.temporal.ChronoUnit.DAYS.between(
                    convertToLocalDate(existingHoliday.getDDebut()), 
                    convertToLocalDate(existingHoliday.getDFin())
                ) + 1;

                double newBalance = availableBalance + oldDaysRequested - daysRequested;

                if (newBalance < 0) {
                    viewH.showMessage("Solde insuffisant après modification des dates.");
                    return;
                }

                // Mise à jour du solde dans la base de données
                modelH.updateSoldeDisponible(empId, newBalance);
            }

            // Mise à jour des informations du congé
            Holiday updatedHoliday = new Holiday(
                holidayId, 
                convertToDate(startDate), 
                convertToDate(endDate), 
                type, 
                empId
            );
            modelH.updateHoliday(updatedHoliday);

            // Actualisation de la vue
            viewH.refreshTable();
        } catch (DateTimeParseException e) {
            viewH.showMessage("Format de date invalide. Utilisez yyyy-MM-dd.");
        } catch (Exception e) {
            e.printStackTrace();
            viewH.showMessage("Erreur lors de la modification du congé.");
        }
    }


    // Method to delete a holiday
    public void deleteHoliday() {
        try {
            // Retrieve selected holiday ID
            int holidayId = viewH.getSelectedHolidayId();

            if (holidayId == -1) {
                viewH.showMessage("Veuillez sélectionner un congé à supprimer.");
                return;
            }

            // Delete the holiday
            modelH.deleteHoliday(holidayId);

            viewH.refreshTable();

        } catch (Exception e) {
            e.printStackTrace();
            viewH.showMessage("Erreur lors de la suppression du congé.");
        }
    }
}
