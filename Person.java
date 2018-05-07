import org.jetbrains.annotations.NotNull;

public class Person implements Comparable{
    private String firstName;
    private String lastName;
    private int age;

    Person(String firstName, String lastName, int age){
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public String getLastName(){
        return lastName;
    }

    public int compareTo(@NotNull Object o){
        Person p = (Person) o;
        return p.getLastName().compareTo(this.lastName);
    }

    public void print(){
        System.out.println(firstName + " " + lastName);
    }

    public static void main(String[] args){
        Person[] people = new Person[7];
        people[0] = new Person("shalom", "azar", 21);
        people[1] = new Person("breban", "yu", 28);
        people[2] = new Person("Israel", "best", 69);
        people[3] = new Person("leave", "america", 270);
        people[4] = new Person("jason", "king", 65);
        people[5] = new Person("josh", "azar", 24);
        people[6] = new Person("torah", "umada", 100);

        myFunc sort = (Person[] p) ->{
            Person temp = null;
            int loc = 0;
            for(int i = 0; i < p.length; i ++){
                for(int j = 0; j < p.length; j ++){
                    if(p[i].compareTo(p[j]) < 0){
                        temp = p[i];
                        p[i] = p[j];
                        p[j] = temp;
                    }
                }
            }

            return p;
        };


        people = sort.func(people);
        System.out.println("Everything Sorted:");
        for (int i = 0; i < people.length; i ++){
            people[i].print();
        }
    }

}

interface myFunc{
    public Person[] func (Person[] people);
}

