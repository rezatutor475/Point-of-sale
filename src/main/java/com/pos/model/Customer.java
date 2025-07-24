package com.pos.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

/**
 * Customer
 *
 * Represents customer details such as identity, contact, and loyalty attributes.
 */
public class Customer {

    private String id;
    private String fullName;
    private String phoneNumber;
    private String email;
    private LocalDate dateOfBirth;
    private boolean loyal;
    private int loyaltyPoints;
    private String address;
    private String membershipLevel;
    private LocalDate registrationDate;
    private String referredByCustomerId;
    private boolean active;
    private String notes;

    public Customer() {
        this.registrationDate = LocalDate.now();
        this.membershipLevel = "Basic";
        this.active = true;
        this.notes = "";
    }

    public Customer(String id, String fullName, String phoneNumber, String email, LocalDate dateOfBirth) {
        this();
        this.id = id;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public boolean isLoyal() {
        return loyal;
    }

    public void setLoyal(boolean loyal) {
        this.loyal = loyal;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = Math.max(0, loyaltyPoints);
    }

    public void addLoyaltyPoints(int points) {
        if (points > 0) {
            this.loyaltyPoints += points;
        }
    }

    public void redeemLoyaltyPoints(int points) {
        if (points > 0 && points <= this.loyaltyPoints) {
            this.loyaltyPoints -= points;
        }
    }

    public boolean canRedeemPoints(int requiredPoints) {
        return requiredPoints > 0 && loyaltyPoints >= requiredPoints;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMembershipLevel() {
        return membershipLevel;
    }

    public void setMembershipLevel(String membershipLevel) {
        this.membershipLevel = membershipLevel;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public boolean isRecentlyRegistered() {
        return registrationDate != null && registrationDate.isAfter(LocalDate.now().minusMonths(1));
    }

    public String getReferredByCustomerId() {
        return referredByCustomerId;
    }

    public void setReferredByCustomerId(String referredByCustomerId) {
        this.referredByCustomerId = referredByCustomerId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void deactivate() {
        this.active = false;
    }

    public void upgradeMembership(String newLevel) {
        if (newLevel != null && !newLevel.equalsIgnoreCase(this.membershipLevel)) {
            this.membershipLevel = newLevel;
        }
    }

    public boolean isPremiumMember() {
        return "Premium".equalsIgnoreCase(membershipLevel);
    }

    public boolean isBirthdayToday() {
        return dateOfBirth != null &&
               LocalDate.now().getMonth() == dateOfBirth.getMonth() &&
               LocalDate.now().getDayOfMonth() == dateOfBirth.getDayOfMonth();
    }

    public int getAge() {
        return (dateOfBirth != null) ? Period.between(dateOfBirth, LocalDate.now()).getYears() : -1;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", fullName='" + fullName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", loyal=" + loyal +
                ", loyaltyPoints=" + loyaltyPoints +
                ", address='" + address + '\'' +
                ", membershipLevel='" + membershipLevel + '\'' +
                ", registrationDate=" + registrationDate +
                ", referredByCustomerId='" + referredByCustomerId + '\'' +
                ", active=" + active +
                ", notes='" + notes + '\'' +
                '}';
    }
}
