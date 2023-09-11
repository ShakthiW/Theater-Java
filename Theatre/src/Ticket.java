public class Ticket {
    private int row;
    private int seat;
    private int price;
    private Person person;

    public Ticket(int row, int seat, int price, Person person) {
        this.row = row;
        this.seat = seat;
        this.price = price;
        this.person = person;
    }

    public void print() {
        System.out.println("Name : " + person.getName());
        System.out.println("Surname : " + person.getSurname());
        System.out.println("E-mail : " + person.getEmail());
        System.out.println("Row Number : " + row);
        System.out.println("Seat Number : " + seat);
        System.out.println("Price : $" + price);
    }

    public int getRow() {
        return row;
    }

    public int getSeat() {
        return seat;
    }
    public Person getPerson() {
        return person;
    }

    public int getPrice() {
        return price;
    }
}
