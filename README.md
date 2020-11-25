# `specifikation`
A Kotlin library that verifies if an object meets a specification

A [specification][wikipedia:specification]

> refers to a set of documented requirements to be satisfied by a material, design, product, or service. A specification is often a type of technical standard. 

## Design
A `Specification<Subject, Violation>` is an interface and implementors should implement the `isMetBy` method

```kotlin
    fun isMetBy(subject: Subject): Report<Violation>
```

an element of type `Subject` is the subject under test and `Report<Violation>` serves as a report about if the subject under test successfully adheres to the specification.

`Report<Violation>` is a [sealed class][kotlin:sealed-class] having two subclasses: `Success` and `Failure`. The `Failure` constructor accepts a list of `Violation`s that can inform the client why the subject under test does not adhere to the specification.

## Usage
We would like to use the `specifikation` library to validate objects as follows. Let's say we have created a `Specification<Person, String>` and a candidate `Person` object.

```kotlin
val report: Report<String> = specification.isMetBy(person)

when (report) {
  is Success { /* person passed specification */ }
  is Failure { violations = report.violations }
}
```

## Prior Art
The `specifikation` library is not the only Kotlin library that can validate if an object meets a specification. We are at least aware of, and have examined the following libraries.

* [`konform`][library:konform]
* [`kalidation`][library:kalidation]
* [`valiktor`][library:valiktor]

These are all great libraries. Unfortunately neither of these libraries fitted our needs. That in part was the motivation for creating this library, even though we are familiar with [xkcd:927][].

![Fortunately, the charging one has been solved now that we've all standardized on mini-USB. Or is it micro-USB? Shit.](https://imgs.xkcd.com/comics/standards.png)

Below you will find some of the reasons above libraries do not fit our needs.

### `konform`
The [initial impetus][konform:impetus] to create `konform`

> building a kind of generator to transform a limit subset of a JSON Schema code to Konform later. Therefore I started with implementing validations that are like what can be found there.

Rules that are outside of JSON Schema code have limited support. Although it is possible to add custom rules, it involves a setting up a bit of boilerplate.


### `kalidation`
Rules in `kalidation` are subclasses of a [sealed][kotlin:sealed-class] class `ConstraintRule`. There is no way to add a custom rule to kalidation, other than [testing a function][kalidation:issue:4] on the object under test.

This robs us the oppertunity to specify rules specific to our domain.

### `valiktor`
An object not meeting a specification is not exceptional to us. We receive objects in bulk, over the wire, from various sources. If a violation would be signaled by an exception, processing the collection would be difficult.

`valiktor` throws an `ConstraintViolationException` when an object does not meet a specification. Therefore it is not a nice fit.


[wikipedia:specification]: https://en.wikipedia.org/wiki/Specification_(technical_standard)
[kotlin:sealed-class]: https://kotlinlang.org/docs/reference/sealed-classes.html
[library:konform]: https://www.konform.io/
[library:kalidation]: https://github.com/rcapraro/kalidation
[library:valiktor]: https://github.com/valiktor/valiktor
[xkcd:927]: https://xkcd.com/927/
[konform:impetus]: https://github.com/konform-kt/konform/issues/4
[kalidation:issue:4]: https://github.com/rcapraro/kalidation/issues/4

