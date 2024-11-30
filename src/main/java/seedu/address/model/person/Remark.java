package seedu.address.model.person;

/**
 * Represents a Person's remark in the address book.
 * Guarantees: immutable; is always valid as declared in the constructor.
 */
public class Remark {

    /** The remark text for the person. */
    public final String value;

    /**
     * Constructs a {@code Remark}.
     *
     * @param remark A valid remark string.
     */
    public Remark(String remark) {
        this.value = remark;
    }

    /**
     * Returns the string representation of the remark.
     *
     * @return The remark as a string.
     */
    @Override
    public String toString() {
        return value;
    }

    /**
     * Returns true if both remarks have the same value.
     * This defines a stronger notion of equality between two remarks.
     *
     * @param other The other object to compare to.
     * @return true if both remarks have the same value.
     */
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Remark // instanceof handles nulls
                && value.equals(((Remark) other).value)); // state check
    }
}