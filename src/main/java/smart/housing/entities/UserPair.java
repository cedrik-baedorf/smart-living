package smart.housing.entities;


public class UserPair {
    private final User firstUser;
    private final User secondUser;

    public UserPair(User firstUser, User secondUser) {
        this.firstUser = firstUser;
        this.secondUser = secondUser;
    }

    public User getFirstUser() {
        return firstUser;
    }

    public User getSecondUser() {
        return secondUser;
    }

    // Override equals and hashCode methods to ensure correct behavior in HashMap
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserPair userPair = (UserPair) o;

        return (firstUser.equals(userPair.firstUser) && secondUser.equals(userPair.secondUser)) ||
                (firstUser.equals(userPair.secondUser) && secondUser.equals(userPair.firstUser));
    }

    @Override
    public int hashCode() {
        int result = firstUser.hashCode();
        result = 31 * result + secondUser.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "UserPair{" +
                "firstUser=" + firstUser +
                ", secondUser=" + secondUser +
                '}';
    }
}