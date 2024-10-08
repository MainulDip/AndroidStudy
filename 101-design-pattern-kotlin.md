## Overview:
Some common design pattern with quick example

### Creational Design pattern:
Creational patterns are ones that create objects, rather than having to instantiate objects directly. This gives the program more flexibility in deciding which objects need to be created for a given case.

`Abstract factory` groups object factories that have a common theme.
`Builder` constructs complex objects by separating construction and representation.
`Factory` method creates objects without specifying the exact class to create.
`Prototype` creates objects by cloning an existing object.
`Singleton` restricts object creation for a class to only one instance.

### Structural:
Structural patterns concern class and object composition. They use inheritance to compose interfaces and define ways to compose objects to obtain new functionality.

`Adapter` allows classes with incompatible interfaces to work together by wrapping its own interface around that of an already existing class.
`Bridge` decouples an abstraction from its implementation so that the two can vary independently.
`Composite` composes zero-or-more similar objects so that they can be manipulated as one object.
`Decorator` dynamically adds/overrides behaviour in an existing method of an object.
`Facade` provides a simplified interface to a large body of code.
`Flyweight` reduces the cost of creating and manipulating a large number of similar objects.
`Proxy` provides a placeholder for another object to control access, reduce cost, and reduce complexity.

### Behavioral:
Most behavioral design patterns are specifically concerned with communication between objects.

`Chain of responsibility` delegates commands to a chain of processing objects.
`Command` creates objects that encapsulate actions and parameters.
`Interpreter` implements a specialized language.
`Iterator` accesses the elements of an object sequentially without exposing its underlying representation.
`Mediator` allows loose coupling between classes by being the only class that has detailed knowledge of their methods.
`Memento `provides the ability to restore an object to its previous state (undo).
`Observer` is a publish/subscribe pattern, which allows a number of observer objects to see an event.
`State` allows an object to alter its behavior when its internal state changes.
`Strategy` allows one of a family of algorithms to be selected on-the-fly at runtime.
`Template` method defines the skeleton of an algorithm as an abstract class, allowing its subclasses to provide concrete behavior.
`Visitor` separates an algorithm from an object structure by moving the hierarchy of methods into one object.

### Other Types Of Pattern:
`dependency injection` is a programming technique in which an object or function receives other objects or functions that it requires, as opposed to creating them internally.
`delegation` pattern is an object-oriented design pattern that allows object composition to achieve the same code reuse as inheritance.
`repository pattern` is a design pattern that isolates the data layer from the rest of the app
### Builder Design Pattern
It's a pattern to build up a complex object in a step by step approach.
```kotlin
open class Item(val name: String, val price: Int)

class Noodles : Item("Noodles", 10) {
    override fun toString(): String {
        return name
    }
}

class Vegetables(vararg val toppings: String) : Item("Vegetables", 5) {
    override fun toString(): String {
        if (toppings.isEmpty()) {
            return "$name Chef's Choice"
        } else {
            return name + " " + toppings.joinToString()
        }
    }
}

class Order(val orderNumber: Int) {
    private val itemList = mutableListOf<Item>()

    fun addItem(newItem: Item): Order {
        itemList.add(newItem)
        return this
    }

    fun addAll(newItems: List<Item>): Order {
        itemList.addAll(newItems)
        return this
    }

    fun print() {
        println("Order #${orderNumber}")
        var total = 0
        for (item in itemList) {
            println("${item}: $${item.price}")
            total += item.price
        }
        println("Total: $${total}")
    }
}

fun main() {
    val ordersList = mutableListOf<Order>()

    // Add an item to an order
    val order1 = Order(1)
    order1.addItem(Noodles())
    ordersList.add(order1)

    // Add multiple items individually
    val order2 = Order(2)
    order2.addItem(Noodles())
    order2.addItem(Vegetables())
    ordersList.add(order2)

    // Add a list of items at one time
    val order3 = Order(3)
    val items = listOf(Noodles(), Vegetables("Carrots", "Beans", "Celery"))
    order3.addAll(items)
    ordersList.add(order3)

    // Use builder pattern
    val order4 = Order(4)
        .addItem(Noodles())
        .addItem(Vegetables("Cabbage", "Onion"))
    ordersList.add(order4)

    // Create and add order directly
    ordersList.add(
        Order(5)
            .addItem(Noodles())
            .addItem(Noodles())
            .addItem(Vegetables("Spinach"))
    )

    // Print out each order
    for (order in ordersList) {
        order.print()
        println()
    }
}

```


### Design Pattern Common:
* Adapter: Adapter is a design pattern that adapts the data into something that can be used by RecyclerView. Usually adapter fetch data from a datastore/databases and format the data to feed the ui/recyclerview

* ViewHolders: RecyclerView doesn't interact directly with item views, but deals with ViewHolders. A ViewHolder represents a single list item view in RecyclerView, and can be reused when possible. A ViewHolder instance holds references to the individual views within a list item layout (hence the name "view holder"). This makes it easier to update the list item view with new data. View holders also add information that RecyclerView uses to efficiently move views around the screen
* LayoutInflater: inflate an XML layout into a hierarchy of view objects. Or simply it converts xml layout file into a view objects

### Android Architectural Principles:
* The main principles are separation of concerns and driving UI from a model.
 - Separation of concerns: The separation of concerns design principle states that the app should be divided into classes, each with separate responsibilities.
 - Drive UI from a model:  UI should drive/come from a model, preferably a persistent model. Models are components that are responsible for handling the data for an app. They're independent from the Views and app components, so they're unaffected by the app's lifecycle and the associated concerns.

 * The main classes or components in Android Architecture are UI Controller (activity/fragment), ViewModel, LiveData and Room.

 ### Repository Design Pattern:
 The repository pattern is a design pattern that isolates the data layer from the rest of the app. The data layer refers to the part of the app, separate from the UI, that handles the app's data and business logic, exposing consistent APIs for the rest of your app to access this data.
 <img src="./Images/other-images/repository-design-pattern.png"/>

 ### ViewModel:
 It's a Model to feed data to View(UI Controller's like activity/fragment) Closely. It separate the data from the UI/View so that the data/state can persist until UI finishes (garbage collected).

 * the viewModel is created by delegating viewModels() UI Controller's method so that the Android system can persist the viewModel object state internally and keep that until the UI is garbage collected or finished.

 ```kotlin
 // 1. add the ViewModel Dependencies inside module's build.gradle
 // 2. create Model class Inheriting form ViewModel()
class GameViewModel: ViewModel() {}
 // 3. Inside UI-Controller (Framgment/Activity) delegate property of the Model class by viewModels()
 private val viewModel: GameViewModel by viewModels()
 ```

 ### UI Controllers(activity/fragment) vs ViewModel:
 The Android system can destroy UI controllers at any time based on certain user interactions or because of system conditions like low memory. Thats why it's not a good place to store app's state. Instead, the decision-making logic about the data should be added in your ViewModel. The ViewModel stores the app related data that isn't destroyed when activity or fragment is destroyed and recreated by the Android framework

 * Activities and fragments are responsible for drawing views and data to the screen and responding to the user events. As licycle of this is not on developers hand, application state should never live here.

 * ViewModel is responsible for holding and processing all the data needed for the UI. It should never access the view hierarchy (like view binding object) or hold a reference to the activity or the fragment. It will only process and deliver the data to the UI controllers.

 ### ViewBindings
View binding is a feature that allows you to more easily access views in code. It generates a binding class for each XML layout file. An instance of a binding class contains direct references to all views that have an ID in the corresponding layout.

* Binding is accomplished by inflating the layout file with the activity/fragment Binding object. Binding is also a View Object.
```kotlin
// top level
private lateinit var binding: GameFragmentBinding
// inside specific lifecycle method
binding = GameFragmentBinding.inflate(inflater, container, false)

return binding.root // binding.root is the parent View containing all the child views

// inside another lificycle method
binding.textViewUnscrambledWord.text = newWord
binding.score.text = getString(R.string.score, newScore)
binding.wordCount.text =
                  getString(R.string.word_count, newWordCount, MAX_NO_OF_WORDS)
```

### LiveData and Observer:
* LiveData is a observable data that can update the specific data ui when state changes
* Observer is a concept that automatically update the ui if the targated live data changes
```kotlin
// defining livedata | Usually inside a viewmodel class
private val _currentWordCount = MutableLiveData<Int>(0) // default value is 0
val currentWordCount: LiveData<Int> get() = _currentWordCount // public getter
    
private val _currentScrambledWord = MutableLiveData<String>() // no initial/default value
val currentScrambledWord: LiveData<String> get() = _currentScrambledWord // public getter


// setting live data as observable inside UI controller, like activity or fragment class's specific lifycle method (when the view is created)
// so if the observed live data changes, everytime the callback will be tiggered.
viewModel.currentWordCount.observe(viewLifecycleOwner) { newW ->
    binding.wordCount.text = getString(R.string.word_count, newW, MAX_NO_OF_WORDS)
}
viewModel.currentScrambledWord.observe(viewLifecycleOwner, Observer { newWord ->
    binding.textViewUnscrambledWord.text = newWord
})


```

### DataBinding and LiveData:
Data binding binds the UI components in layouts to data sources using a declarative format. It's a part of the Android Jetpack library.
* In simpler terms Data binding is binding data (from code) to views + view binding (binding views to code).
```kotlin
// Here viewModel is the delegated GameViewModel : ViewModel() instance.
// ViewBinding in UI Controllers view created lificycle method
binding.textViewUnscrambledWord.text = viewModel.currentScrambledWord

// Databindings in layout xml file. 
// Here gameViewModel is the delegated GameViewModel : ViewModel() instance.
android:text="@{gameViewModel.currentScrambledWord}"
```

* Steps For DataBindings:
 - build.gradle add buildFeatures => dataBinding = true and add => id 'kotlin-kapt as plugins
    - this generates a binary file for every layout xml file. For activity_main.xml, the auto generated class will be ActivityMainBinding (Like View Binding)
 
 - To use DataBinding, the layout file will start with <layout> followed by an optional <data> element and a view (ScrollView or other type of view) root element.
    - use IDE feature to auto convert into databinding layout by alt+enter while keeping cursor on the parent view and select "convert to databinding......."
 
 ```xml
 <layout xmlns:android="http://schemas.android.com/apk/res/android"
   xmlns:app="http://schemas.android.com/apk/res-auto"
   xmlns:tools="http://schemas.android.com/tools">

   <data>

   </data>

   <ScrollView
       android:layout_width="match_parent"
       android:layout_height="match_parent">

       <androidx.constraintlayout.widget.ConstraintLayout
         ...
       </androidx.constraintlayout.widget.ConstraintLayout>
   </ScrollView>
</layout>
 ```
 
 - instantiate binding as DataBindingUtil on "onCreateView" (for fragment) lifecycle method
    - like => binding = DataBindingUtil.inflate(inflater, R.layout.game_fragment, container, false)
    - instade of viewBinding => binding = GameFragmentBinding.inflate(inflater, container, false)
    - Note: the lateinit declaration of the ViewModel will remain same like ViewBinding => private val viewModel: GameViewModel by viewModels()

- bind the layout variables and lifecycleOwner with the binding object in onCreateView (for fragment) lifecycle method. Like

```kotlin
 binding.gameViewModel = viewModel
 binding.maxNoOfWords = MAX_NO_OF_WORDS
 binding.lifecycleOwner = viewLifecycleOwner
```
- connect layout view using binding expression "@{}" with the layout's declared variables directly, like

```xml
<!-- databinding layout attaching variables with view -->
<data>
    <variable
        name="gameViewModel"
        type="com.example.android.unscramble.ui.game.GameViewModel" />

    <variable
        name="maxNoOfWords"
        type="int" />
</data>
...
<TextView
   android:id="@+id/textView_unscrambled_word"
   ...
   android:text="@{gameViewModel.currentScrambledWord}"
   .../>
``` 
- No LiveData Observer Required: The layout (xml) will receive the updates of the changes to the LiveData defined in the custom viewModel (ViewModel() inherited classes) through data binding in variables

### Resources in data binding expressions:
A data binding expression can reference app resources using
```xml
<!-- layout.xml || pass the value as function params -->
android:text="@{@string/example_resource(user.lastName)}"

<!-- strings.xml, here %s will be replaced with "user.lastname" defined in layout file above -->
<string name="example_resource">Last Name: %s</string>
```

### LiveData to Talkback:
Convert LiveData to LiveData<Spanable> and transform
```kotlin
private val _currentScrambledWord = MutableLiveData<String>()
val currentScrambledWord: LiveData<Spannable> get() = Transformations.map(_currentScrambledWord) {
    if (it == null) {
        SpannableString("")
    } else {
        val scrambledWord = it.toString()
        val spannable: Spannable = SpannableString(scrambledWord)
        spannable.setSpan(
            TtsSpan.VerbatimBuilder(scrambledWord).build(),
            0,
            scrambledWord.length,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )
        spannable
    }
}
```

### Caching | Repository Pattern:
The repository pattern is a design pattern that isolates the data layer from the rest of the app (UI and ViewModels). A repository can resolve conflicts between data sources (such as persistent models, web services, and caches) and centralize changes to this data. The Repository class implements the logic for deciding whether to fetch data from a network or use results that are cached in a local database.

### refresh strategy:
A database refresh is a process of updating or refreshing the local database to keep it in sync with data from the network. Like the module that requests data from the repository is responsible for refreshing the local data.
### Factory Pattern:
In Factory pattern, the object is created without exposing the logic to the client/caller side. And refer to the newly created object using a common interface/method-name
```java
public interface Shape {
   void draw();
}

public class Rectangle implements Shape {

   @Override
   public void draw() {
      System.out.println("Inside Rectangle::draw() method.");
   }
}

public class Square implements Shape {

   @Override
   public void draw() {
      System.out.println("Inside Square::draw() method.");
   }
}

public class Circle implements Shape {

   @Override
   public void draw() {
      System.out.println("Inside Circle::draw() method.");
   }
}

public class ShapeFactory {
	
   //use getShape method to get object of type shape 
   public Shape getShape(String shapeType){
      if(shapeType == null){
         return null;
      }		
      if(shapeType.equalsIgnoreCase("CIRCLE")){
         return new Circle();
         
      } else if(shapeType.equalsIgnoreCase("RECTANGLE")){
         return new Rectangle();
         
      } else if(shapeType.equalsIgnoreCase("SQUARE")){
         return new Square();
      }
      
      return null;
   }
}

public class FactoryPatternDemo {

   public static void main(String[] args) {
      ShapeFactory shapeFactory = new ShapeFactory();

      //get an object of Circle and call its draw method.
      Shape shape1 = shapeFactory.getShape("CIRCLE");

      //call draw method of Circle
      shape1.draw();

      //get an object of Rectangle and call its draw method.
      Shape shape2 = shapeFactory.getShape("RECTANGLE");

      //call draw method of Rectangle
      shape2.draw();

      //get an object of Square and call its draw method.
      Shape shape3 = shapeFactory.getShape("SQUARE");

      //call draw method of square
      shape3.draw();
   }
}
```
### Inversion of Control (IoC)
Inversion of Control (IoC) means to create instances of dependencies first and latter instance of a class (optionally injecting them through constructor), instead of creating an instance of the class first and then the class instance creating instances of dependencies. Thus, inversion of control inverts the flow of control of the program. Instead of the callee controlling the flow of control (while creating dependencies), the caller controls the flow of control of the program.
Event-driven programming is often implemented using IoC so that the custom code need only be concerned with the handling of events, while the event loop and dispatch of events/messages is handled by the framework or the runtime environment. In web server application frameworks, dispatch is usually called routing, and handlers may be called endpoints.




### Pragma | Pragmatic Information:
Pragma is short for pragmatic information. Pragmatic means "sensible and practical". In programming, pragma refers to:
 - The code that consists of useful information on how a compiler or interpreter or assembler should process the program
 - pragma does not contribute anything to the programming language itself, pragma only affects the compiler behavior
 - pragma‘s syntax may vary.


### Macro (macroinstruction)
It's a programmable pattern that translates a certain sequence of input into a preset sequence of output. In the general world, a macro can be a keyboard shortcut/hotkey to do multiple tasks (not a single task).
In programming, A macro is a name given to a block of C statements as a pre-processor directive. Being a pre-processor, the block of code is communicated to the compiler before entering into the actual coding (main () function). A macro is defined with the pre-processor directive.

* Macro vs Functions: Macros are pre-processed which means that all the macros would be processed before your program compiles. However, functions are not preprocessed but compiled.

```c
// macro
#include<stdio.h>
#define NUMBER 10
int main()
{
     printf("%d", NUMBER);
     return 0;
}

// function
#include<stdio.h>
int number()
{
    return 10;
}
int main()
{
    printf("%d", number());
    return 0;
}
```

### GCC & G++ :
* GCC : C compiler, which stands for "GNU Compiler Collection"
* G++ : C++ compiler, which stands for "GNU C++" compiler

NB: Both can be used with c and c++, they are the same, just different front-end (will justify later)

### Android App Structure:
Model-View-ViewModel with Repository

UI/View Layer

Domain Layer (optional utils class with `UseCase` suffix each with a singular job) to apply further business logic on Data from the `ViewModel Layer`. Sometime Domain layer is replaced by `Utils` functions. But applying `Domain Layer` is a good practice.

ViewModel Layer: Apply business logics on Data and forward to Domain or UI/View Layers

Data Layer : Consist of `Repository`, `DataSource` and Database `Models` layers, where repository collects data form DataSources and apply business logics that involve sort/merge/manage data to confirm appropriate shape from different sources.  
