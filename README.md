# Turtle 🐢
A simple Java code generator
# Introduction
Turtle (🐢) is a simple command line application written in Java that generates allows users to use yaml to create Java files. This application uses picocli and SnakeYaml. This app was inspired by Bertil Muth's article on medium about [Generating Data Classes in Java](https://medium.com/free-code-camp/how-to-generate-data-classes-in-java-fead8fa354a2) and also inspired by Ruby on Rails syntax on generating models. Turtle can currently help create class, record, and interface files.
# How to install
## Requirements 
JRE 1.8 or newer to run the app  
JDK 1.8 or newer to develop
## Installing/Running app
1. Ensure you are meeting the requirements above.    
2. Download the latest .jar file from the releases section.
3. Run `java -jar $AbsolutePathToJar/Turtle.jar $args` where `$AbsolutePathToJar` is the actual Absolute Path and `$args` are the actual arguments required to run this app
### Create an Alias (Optional)
Running something like `java -jar $AbsolutePathToJar/Turtle.jar $args` can get very long and tedious to do, which is why it is recommended to create an alias (permanent alias if you plan on using Turtle for the long run) to something like `turtle` so that it looks like you are running a regular command line application and it's a bit easier to run the app. The rest of the README will assume that an alias called `turtle` has been set.
#### Linux
To create an alias in a linux environment run the command: `alias turtle='java -jar $AbsolutePathToJar/Turtle.jar'` so now you can just run `turtle $args` where `$args` is the actual arguments required to run the application.
#### Windows/Powershell
To create an alias in a Powershell enviornment you can create a function.  
```
function turtle {
  java -jar '$AbsolutePathToJar/Turtle.jar $args' 
}
```
Where `$AbsolutePathToJar` is the actual Absolute Path; however note: `$args` is not the actual arguments to run the app. Rather it's part of the Powershell function syntax to be able to accept arguments, so make sure that the last part of the line actually includes `$args` and NOT the actual arguments, as without it, you will not be able to pass in any arguments to the app.
# Running Turtle
## `turtle`
Just running `turtle` will give currently give an output of `Turtle!`
## Generating File directly through the Command Line
As stated before Turtle was inspired by Ruby on Rails' ability to quickly generate Models through the command line. Turtle loosley tries to follow this syntax to help create Java files.
### Creating a Class
A Java class can be created using Turtle by running the command `turtle generate-class $name $field:fieldType...` where `$name` is the name of the class and `$field:fieldType...` is one or more field (with it's type).  
For example if we wanted to create a Person class with the fields: name of type String and an age of type int; we can run the command `turtle generate-class Person name:String age:int` which will go ahead and generate the name and age fields, along with the contstructors and getters and setters at the current directory. 
The generated code will look something like this:  
`Person.java`
```
class Person {

	/* Fields */

	private String name;
	private int age;

	/* Constructors */

	Person(){}

	Person(String name, int age) {
		this.name = name;
		this.age = age;
	}

	/* Getters and Setters */

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		 this.name = name;
	}

	public int getAge() {
		return this.age;
	}

	public void setAge(int age) {
		 this.age = age;
	}
}
```
The `generate-class` command offers many shorthands one of them being `gc`. If we want to generate a file at a subdirectory we can use the `--path` or `-p` option to specify a sub directory. An example of this would be if I am in a directory called 'Project' and that directory had a subdirectory called 'java' I and I wanted my java file to be in the 'java' folder I could run the command `turtle gc -p java Person name:String age:int`. Note: when using the `-p` option only relative directories are supported.
##### Aside
Turtle isn't just limited to primitive types you can use anything as a type. For example you can run the command `turtle gc -p java Person name:String age:int friends: ArrayList<Person>`
#### Extends/Implements
You can extend another class, or implement an interface (or multiple interfaces) in Turtle as well.  
An example would be a ToyPoodle class extending the Dog class and implementing the Poodle Interface: `turtle gc ToyPoodle extends:Dog implements:Poodle name:String age:int`.  
If a class is implementing multiple interfaces you would have to wrap it around single or double quotes.  
An example would be: `turtle gc ToyPoodle extends:Dog implements:'Poodle, Pet' name:String age:int`
#### Creating a field with default value
Turtle also supports creating fields with default values. For example if our Person class had a field student of type boolean and we wanted to set a default to it, it would look like: `turtle gc Person name:String age:int 'student = false:boolean'`. 
  
The generated code would look like this:  
`Person.java`
```
class Person {

	/* Fields */

	private String name;
	private int age;
	private boolean student = false;

	/* Constructors */

	Person(){}

	Person(String name, int age, boolean student) {
		this.name = name;
		this.age = age;
		this.student = student;
	}

	Person(String name, int age) {
		this.name = name;
		this.age = age;
	}

	/* Getters and Setters */

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		 this.name = name;
	}

	public int getAge() {
		return this.age;
	}

	public void setAge(int age) {
		 this.age = age;
	}

	public boolean isStudent() {
		return this.student;
	}

	public void setStudent(boolean student) {
		 this.student = student;
	}
}
```
Notice how the student field has false assigned to it. Also notice how there are three constructors instead of two. Since a default value was provided for the student field, a constructor without the student field is also generated! Yet another thing to notice is that the getter method for student is called `isStudent()` rather than `getStudent()`. In Turtle, boolean getters start with is, rather than get.  
  
Since `student = false:boolean` contains spaces you need to wrap the declaration with single quotes or double quotes in order to tell the command line that the entire declaration is one thing, rather than seperate things. Meaning this would look like `'student = false:boolean'` or `"student = false:boolean"`. Single quotes are recommended since you might need to assign a String value to a field and some command lines allow '""' without having to use the escape characer (`\"`).
##### Aside
When creating a default value that is a String you will need to wrap the value in "".  
Example: `turtle gc Person 'name = "Shubham":String' age:int`.  
You may also have to use `\"` to specify that you want the `""` to show up depending on your command line: `turtle gc Person 'name = \"Shubham\":String age:int`
#### Creating Methods
Turtle also allows for generating method signatures and even short, one liner, method bodies. The simplest way to generate a method signature in Turtle is by using the syntax `methodName#returnType:()` where `methodName` is the name of the method, `returnType` is the method return type and `()` contains the parameter list. In Turtle, generating a method for a class generates a `public` method by default.  
  
Example: If we wanted to create a method with the signature of `public void goBucks()` in Turtle, the syntax would look like this: `goBucks#void:()`. In practice, if this method was part of the Person class the entire syntax would look like this: `turtle gc Person name:String age:int 'student = false:boolean' goBucks#void:()`.
  
Another Example (Including a parameter list): Lets say we had a Random class we wanted to generate, it would contain no fields, but we wanted to include an area method that would return the area of a rectangle, but we also want this method to be public nonstatic method. The syntax would look like this: `turtle gc Random area#int:(int length, int width)`. Note, the parameter list looks exactly like it does when writing actual java code. That's because it is.
##### Method signatures and the `>` Operator
Remember, by default Turtle makes methods in classes public. In order to make it something different we can use the `>` operator. Recall the Random class example from the previous section, if we wanted to make the area method a public static method we could write something like: `'area#int > public static:(int length, int width)'`. Or practically: `turtle gc Random 'area#int > public static:(int length, int width)'` After `> ` we can give the method any signature.
##### Method Return Statement/One liner Statement and the `->` Operator
Turtle also supports writing one liner return statments/regular statements. Suppose we wanted to expand on our area method from the last two sections... We can go ahead and write the return statement as well by using the `->` operator. The syntax would look like this: `'area#int > public static:(int length, int width) -> length * width'`. Or practically:  `turtle gc Random 'area#int > public static:(int length, int width) -> length * width'`  
  
If we had a return type of void we can still use the `->` operator to write the one-liner body. Recall the `goBucks` method. If we wanted to print out 'Go Bucks!' every time the method was called, we can write something like this: `'goBucks#void:() -> System.out.println("Go Bucks!")'` (Note: You may have to use `\"` for some command lines).

##### Complete Example
If we run this command: `turtle gc Person name:String age:int 'student = false:boolean' 'goBucks#void:() -> System.out.println("Go Bucks!")' 'timesAge#int: (int t) -> t * this.age'`  
Turtle will generate:  
`Person.java`
```
class Person {

	/* Fields */

	private String name;
	private int age;
	private boolean student = false;

	/* Constructors */

	Person(){}

	Person(String name, int age, boolean student) {
		this.name = name;
		this.age = age;
		this.student = student;
	}

	Person(String name, int age) {
		this.name = name;
		this.age = age;
	}

	/* Getters and Setters */

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		 this.name = name;
	}

	public int getAge() {
		return this.age;
	}

	public void setAge(int age) {
		 this.age = age;
	}

	public boolean isStudent() {
		return this.student;
	}

	public void setStudent(boolean student) {
		 this.student = student;
	}

	/* Methods */

	public void goBucks() {
		System.out.println("Go Bucks!");
	}

	public int timesAge(int t) {
		return t * this.age;
	}
}
```
### Creating a Record
Turtle supports creating a record through the `generate-record` or `gr` command. The syntax is similar to generating a class. Turtle supports implementing (but not extending) with records as well as methods.
Example: `turtle gr Person name:String age:int 'goBucks#void:() -> System.out.println("Go Bucks")'` generates:  
`Person.Java`
```
record Person(String name, int age) {

	/* Methods */

	public void goBucks() {
		System.out.println("Go Bucks");
	}
}
```
### Creating an Interface
Turtle supports creating an interface through the `generate-interface` or `gi` command. The syntax is similar to generating a class. However, since this is an interface, Turtle only allows the `extends` keyword and not the `interface method`. The interface also does not allow fields, rather takes in methods. These methods do not have bodies, therefore the `->` operator is not supported when generating an interface, and will cause an error, or incorrect java file.  
Example: `turtle gi Rectangle 'area#int:(int length, int width)' 'perimeter#int:(int length, int width)'` generates:  
`Rectangle.java`
```
interface Rectangle{

	 int area(int length, int width);

	 int perimeter(int length, int width);

}
```
Note: Methods are wrapped with single quotes, just incase the command line takes '()' as its own parameter list and not as a String. It is recommended that when trying to generate methods quotes should be used for good measure.
## Generating files through .yml files and `generate`/`g` command
While reading the 'Generating File directly through the Command Line' it might seem that generating some things like methods or fields with default values could be quite tedious. You might have to wrap things around quotes, use escape characters, essentially having to write syntax that might not look as good. Something like `turtle gc Person 'name = \"Shubham\":String age:int student = true:boolean goBucks#void:() -> System.out.println(\"Go Bucks!\")`, looks very messy and the more fields and methods we add, the uglier it will look. Also, what if we want to generate multiple files at once? Currently we'd have to write multiple generate-type commands to generate multiple files. However, Turtle's other feature allows for writing multiple .yml files and then generating them all at once through the `generate` or `g` command.
### Advantages
Writing a .yml file for each class to be generated allows for writing less messy code, and reducing writing unecessary code like "\"" for a double quote to print. Writing all files to be generted in seperate .yml files also allows for all of them to be generated at once, instead of having to write `turtle gc ....` multiple times.
### Drawbacks
While the advantage of writing less messy code, and reducing the amount of times having to write a generate command seems nice, it also comes with some limitiations and drawbacks. Here are some of them:
1. Having to write a new .yml file for each class/record/interface/e.t.c. that needs to be generated.  
Each class/record/interface/e.t.c. declaration needs to be in a seperate .yml file. While this can get tedious, it will help keep things a little cleaner.
2. All .yml files need to be in a directory that already exists and that directory can only contain files that are to be generated.  
The directory where Turtle will read .yml files can only contain files that will be used to generate java files by Turtle.
3. The folder where all generated files will be dropped, must already exist. 
4. The directory where .yml files will be read cannot be the same directory as the generated files directory
5. Absolute directory paths do not work, only relative ones.
### How to use
The syntax for using the `generate` command is: `turtle g yml gen`. Where `yml` was a subfolder in the current directory, this is where all .yml files for Turtle will be. `gen` is the subfolder in the current directory where all generated java files will be.
#### Example
Imagine we have a subdirectory in our current working directory called `turtleYml` where our .yml files are located and a subdirectory called `turtleGen` where we want to our generated java files to be. In our `turtleYml` folder we have the files: `Person.yml`, `Dog.yml`, `Poodle.yml`; and each
file looks like this:  
`Person.yml`
```
class: Person
name: String
age: int
student = false: boolean
career = "unemployed": String
goBucks#void > static: () -> System.out.println("Go Bucks!")
```
`Dog.yml`
```
interface: Dog
bark#void: ()
```
`Poodle.yml`
```
record: Poodle
implements: Dog
name: String
age: int
color: String
```
Notice how each of these files are easier to read than inputting long statementv into a command line. Also, not having to worry about escape characters or when to wrap something with quotes is a plus. We would also have to enter a command for each file to generate, where as now, all we need to enter is: `turtle g turtleYml turtleGen` and all of our java files will be generated into the `turtleGen` folder.  
  
Antother thing to keep in mind is that the yml name could be any name since the name of the class/record/interface comes from the key: value pair and not the file name, however it's better if it has the same name of the class/record/interface name.  
  
Another thing to note, is the space between the ':'... When writing in the terminal the syntax would be `key:value` or as an example `name:String`, but here, in the .yml file it's `name: String`. Make sure that the space is present when writing in .yml files, otherwise there will be an error.
# For the Devs
Turtle uses picocli and SnakeYaml as it's dependencies. It's developed using Java 8.  
Turtle.java contains the main method of the application. The Turtle class also acts as the turtle command, which then has subcommands `generate`, `generate-class`, `generate-record`, and `generate-interface` with there respective classes...  
Generate.java loads up all files in the directory and for each file determines on which object to call (out of ClassFileGenerator, RecordFileGenerator, and InterfaceFileGenerator) to generate the file.  
  
Props.java handles all of the properties (fields, methods, delcarations, name) of a file to be generated.
  
ClassFileGenerator, RecordFileGenerator, and InterfaceFileGenerator are responsible for generating their respective files.  
  
Constants.java contains the constants (at least most of them 😊) used throughout the application.  
  
Utility.java contains some Utility methods used to format Strings, also used for formatting them into YAML.  
  
CLIGenClassCommand, CLIGenRecordCommand, CLIGenInterfaceCommand are responsible for running the (`gc`, `gr`, `gi` commands), taking the String args and formatting it into YAML and then calling the respective FileGenerator object to generate the file.

# Thank You 😁 
Thank you for checking this application/repo out. If you decide to use Turtle, I hope it's helpful! 🐢
