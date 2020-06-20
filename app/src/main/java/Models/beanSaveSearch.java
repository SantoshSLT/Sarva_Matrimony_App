package Models;

/**
 * Created by royal on 22/2/18.
 */

public class beanSaveSearch
{
    String id;
    String ageM;
    String ageF;
    String heightM;
    String heightF;
    String martialStatus;
    String physicalStatus;
    String religion;
    String caste;
    String country;
    String state;
    String city;
    String education;
    String occupation;
    String annualIncome;
    String star;
    String Manglik;
    String searchName;
    String saveDate;

    public beanSaveSearch(String id, String ageM, String ageF, String heightM, String heightF,
                          String martialStatus, String physicalStatus, String religion,
                          String caste, String country, String state, String city, String education,
                          String occupation, String annualIncome, String star, String manglik, String SearchName,String saveDate)
    {
        this.id=id;
        this.ageM = ageM;
        this.ageF = ageF;
        this.heightM = heightM;
        this.heightF = heightF;
        this.martialStatus = martialStatus;
        this.physicalStatus = physicalStatus;
        this.religion = religion;
        this.caste = caste;
        this.country = country;
        this.state = state;
        this.city = city;
        this.education = education;
        this.occupation = occupation;
        this.annualIncome = annualIncome;
        this.star = star;
        this.Manglik = manglik;
        this.searchName = SearchName;
        this.saveDate = saveDate;
    }


    public String getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(String saveDate) {
        this.saveDate = saveDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getAgeM() {
        return ageM;
    }

    public void setAgeM(String ageM) {
        this.ageM = ageM;
    }

    public String getAgeF() {
        return ageF;
    }

    public void setAgeF(String ageF) {
        this.ageF = ageF;
    }

    public String getHeightM() {
        return heightM;
    }

    public void setHeightM(String heightM) {
        this.heightM = heightM;
    }

    public String getHeightF() {
        return heightF;
    }

    public void setHeightF(String heightF) {
        this.heightF = heightF;
    }

    public String getMartialStatus() {
        return martialStatus;
    }

    public void setMartialStatus(String martialStatus) {
        this.martialStatus = martialStatus;
    }

    public String getPhysicalStatus() {
        return physicalStatus;
    }

    public void setPhysicalStatus(String physicalStatus) {
        this.physicalStatus = physicalStatus;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getCaste() {
        return caste;
    }

    public void setCaste(String caste) {
        this.caste = caste;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getAnnualIncome() {
        return annualIncome;
    }

    public void setAnnualIncome(String annualIncome) {
        this.annualIncome = annualIncome;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getManglik() {
        return Manglik;
    }

    public void setManglik(String manglik) {
        Manglik = manglik;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }


}
