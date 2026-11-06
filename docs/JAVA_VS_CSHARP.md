# Java і C# на прикладах

## Клас та інкапсуляція

У Java немає властивостей C#; звичайний клас використовує поля і методи. `record` доречний для DTO. `final` забороняє переприсвоєння посилання, але не робить об'єкт незмінним.

```java
public final class Counter {
    private int value;
    public int getValue() { return value; }
    public void increment() { value++; }
}
```

```csharp
public sealed class Counter {
    public int Value { get; private set; }
    public void Increment() => Value++;
}
```

`public` доступний усім; `private` — всередині класу; відсутність модифікатора у Java означає package-private, а не C# `internal` на рівні assembly. Java `protected` також дає доступ у пакеті. `static` належить класу. Java не має C# `virtual` за замовчуванням: звичайний instance-метод може бути перевизначений, якщо він не `final`, не `private` і не `static`.

## Інтерфейс та OOP

```java
interface Sorter<T> { void sort(T[] values); }
class DemoSorter<T> implements Sorter<T> {
    @Override public void sort(T[] values) { /* реалізація */ }
}
```

```csharp
interface ISorter<T> { void Sort(T[] values); }
class DemoSorter<T> : ISorter<T> {
    public void Sort(T[] values) { /* реалізація */ }
}
```

`implements` означає контракт інтерфейсу, `extends` — успадкування класу/інтерфейсу. Поліморфізм дозволяє CLI працювати з `SortAlgorithm<Integer>` без умов для внутрішньої реалізації. Приватний `Node<T>` зберігає інваріанти списку: зовнішній код не може створити цикл або змінити `previous`.

## Generics та колекції

Java `List<Integer>` використовує boxed `Integer`, не primitive `int`; generics переважно реалізовані через type erasure. Тому не можна написати `new T[n]`. У MergeSort використано `values.clone()`, який зберігає runtime-тип масиву. C# generic типи зберігають більше інформації під час виконання і можуть використовувати value types.

```java
List<String> values = new ArrayList<>();
values.add("Java");
Comparator<String> byLength = Comparator.comparingInt(String::length);
```

```csharp
List<string> values = new();
values.Add("C#");
IComparer<string> byLength = Comparer<string>.Create((a,b) => a.Length.CompareTo(b.Length));
```

`Comparator<? super T>` приймає компаратор самого `T` або його базового типу. `Iterable<T>` дає `for-each`, аналогічно `IEnumerable<T>`. У Java `==` для посилань порівнює identity, а `equals` — логічну рівність; список використовує `Objects.equals`, що підтримує `null`.

## Винятки та ресурси

Java має checked exceptions: метод оголошує `throws IOException` або обробляє виняток. `RuntimeException` та його нащадки — unchecked. C# не має checked exceptions. `try (resource)` аналогічний `using`: закриває `AutoCloseable`, наприклад writer експерименту. Не перехоплюй `Exception` лише для приховування помилки; у CLI помилковий аргумент повинен дати ненульове завершення.

## Unit tests

```java
@Test void removesLastValue() {
    var list = new DoublyLinkedList<String>();
    list.addLast("x");
    assertEquals("x", list.removeLast());
    assertTrue(list.isEmpty());
}
```

```csharp
[Fact] public void RemovesLastValue() {
    var list = new DoublyLinkedList<string>();
    list.AddLast("x");
    Assert.Equal("x", list.RemoveLast());
    Assert.True(list.IsEmpty);
}
```

JUnit 5 annotation `@ParameterizedTest` близький до xUnit `[Theory]`. `mvn test` запускає Surefire; звіти знаходяться в `target/surefire-reports`. Тести — перевірювані сценарії, а не доказ повної відсутності помилок.
