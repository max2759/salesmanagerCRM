package be.atc.salesmanagercrm.validators;

import be.atc.salesmanagercrm.entities.CompaniesEntity;
import be.atc.salesmanagercrm.utils.JsfUtils;
import lombok.extern.slf4j.Slf4j;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Maximilien Zabbara
 */
@Slf4j
public class CompanyValidator {

    public static List<String> validate(CompaniesEntity companiesEntity) {

        List<String> errors = new ArrayList<>();

        Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        FacesMessage msg;


        if (companiesEntity == null) {

            errors.add("Le nom de l'entreprise est obligatoire !");
            errors.add("Veuillez choisir un type d'entreprise");

            msg = new FacesMessage(JsfUtils.returnMessage(locale, "company.labelNotValid"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return errors;
        }

        if (!(companiesEntity.getDomainName().equals(""))) {
            if (!validateUrlDomain(companiesEntity)) {
                errors.add("Le nom de domaine doit être valide !");
                msg = new FacesMessage(JsfUtils.returnMessage(locale, "company.domainNotValid"), null);
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        }

        if (companiesEntity.getLabel() == null || companiesEntity.getLabel().isEmpty()) {
            errors.add("Le nom de l'entreprise est obligatoire !");
            msg = new FacesMessage(JsfUtils.returnMessage(locale, "company.labelNotValid"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

        if (companiesEntity.getCompanyTypesByIdCompanyTypes() == null) {
            errors.add("Veuillez choisir un type d'entreprise");
            msg = new FacesMessage(JsfUtils.returnMessage(locale, "company.companiesTypeNotValid"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

        if (companiesEntity.getEmployeesNumber() != 0) {
            if (numbersIsPositive(companiesEntity)) {
                errors.add("Le nombre entré doit être supérieur à 0");
                msg = new FacesMessage(JsfUtils.returnMessage(locale, "company.employeesNumberNotValid"), null);
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        }


        if (companiesEntity.getRevenue() != null) {
            if (revenueIsPositive(companiesEntity)) {
                errors.add("Le nombre entré doit être supérieur à 0");
                msg = new FacesMessage(JsfUtils.returnMessage(locale, "company.employeesNumberNotValid"), null);
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        }


        if (!(companiesEntity.getLinkedInPage().equals(""))) {
            if (!(validateLinkedinPage(companiesEntity))) {
                errors.add("La page LinkedIn doit être valide !");
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(locale, "company.LinkedinPageNotValid"), null);
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        }

        return errors;
    }

    public static boolean validateUrlDomain(CompaniesEntity companiesEntity) {
        Pattern pattern = Pattern.compile("([https://|http://])*([www.])*\\w+\\.\\w+\\D+");
        Matcher matcher = pattern.matcher(companiesEntity.getDomainName());

        return matcher.find();
    }

    public static boolean validateLinkedinPage(CompaniesEntity companiesEntity) {
        Pattern pattern = Pattern.compile("^https?://((www|\\w\\w)\\.)?linkedin.com/");
        Matcher matcher = pattern.matcher(companiesEntity.getLinkedInPage());

        return matcher.find();
    }

    public static boolean numbersIsPositive(CompaniesEntity companiesEntity) {

        return companiesEntity.getEmployeesNumber() <= 0;
    }

    public static boolean revenueIsPositive(CompaniesEntity companiesEntity) {

        return companiesEntity.getRevenue() <= 0;
    }
}
