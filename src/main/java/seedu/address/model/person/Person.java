package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.doctor.Speciality;
import seedu.address.model.patient.DateOfBirth;
import seedu.address.model.patient.Gender;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;
    private final Remark remark;

    // Data fields
    private final Address address;
    private final Set<Tag> tags = new HashSet<>();
    private final Set<Appointment> appointments = new HashSet<>();

    // Optional fields
    private final Optional<Speciality> speciality;
    private final Optional<DateOfBirth> dateOfBirth;
    private final Optional<Gender> gender;

    /**
     * Main constructor with optional fields.
     */
    public Person(
            Name name,
            Phone phone,
            Email email,
            Address address,
            Remark remark,
            Optional<Speciality> speciality,
            Optional<DateOfBirth> dateOfBirth,
            Optional<Gender> gender,
            Set<Tag> tags
    ) {
        requireAllNonNull(name, phone, email, address, remark, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.remark = remark;
        this.speciality = speciality != null ? speciality : Optional.empty();
        this.dateOfBirth = dateOfBirth != null ? dateOfBirth : Optional.empty();
        this.gender = gender != null ? gender : Optional.empty();
        this.tags.addAll(tags);
    }

    /**
     * Constructor with required person fields only.
     */
    public Person(Name name, Phone phone, Email email, Address address, Remark remark, Set<Tag> tags) {
        this(name, phone, email, address, remark, Optional.empty(), Optional.empty(), Optional.empty(), tags);
    }

    // Getters for identity fields
    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public Remark getRemark() {
        return remark;
    }

    // Getters for optional fields
    public Optional<Speciality> getSpeciality() {
        return speciality;
    }

    public Optional<DateOfBirth> getDateOfBirth() {
        return dateOfBirth;
    }

    public Optional<Gender> getGender() {
        return gender;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns an immutable appointment set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Appointment> getAppointments() {
        return Collections.unmodifiableSet(appointments);
    }

    /**
     * Adds an appointment to the set of appointments.
     * @param appointment The appointment to add.
     * @return Whether the set did not already contain the appointment.
     */
    public boolean addAppointment(Appointment appointment) {
        return appointments.add(appointment);
    }

    /**
     * Removes an appointment from the set of appointments.
     * @param appointment The appointment to remove.
     * @return Whether the appointment was removed.
     */
    public boolean removeAppointment(Appointment appointment) {
        return appointments.remove(appointment);
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && address.equals(otherPerson.address)
                && tags.equals(otherPerson.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phone, email, address, tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("tags", tags)
                .add("speciality", speciality.orElse(null))
                .add("dateOfBirth", dateOfBirth.orElse(null))
                .add("gender", gender.orElse(null))
                .toString();
    }
}
