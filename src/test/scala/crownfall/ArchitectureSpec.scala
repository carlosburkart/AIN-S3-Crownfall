package crownfall

import org.scalatest.funsuite.AnyFunSuite
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices

class ArchitectureSpec extends AnyFunSuite {

  private val classes =
    new ClassFileImporter().importPackages("crownfall")

  // Test 1: Model darf nicht vom Controller abhängen
  test("Model darf nicht vom Controller abhängen") {
    val rule = noClasses()
      .that().resideInAPackage("..model..")
      .should().dependOnClassesThat()
      .resideInAPackage("..controller..")

    rule.check(classes)
  }

  // Test 2: Model darf nicht von der View abhängen
  test("Model darf nicht von der View abhängen") {
    val rule = noClasses()
      .that().resideInAPackage("..model..")
      .should().dependOnClassesThat()
      .resideInAPackage("..aview..")

    rule.check(classes)
  }

  // Test 3: Controller darf nicht von der View abhängen
  test("Controller darf nicht von der View abhängen") {
    val rule = noClasses()
      .that().resideInAPackage("..controller..")
      .should().dependOnClassesThat()
      .resideInAPackage("..aview..")

    rule.check(classes)
  }

  // Test 4: Keine zyklischen Abhängigkeiten zwischen Paketen
  test("Keine zyklischen Abhängigkeiten zwischen Paketen") {
    val rule = slices()
      .matching("crownfall.(*)..")
      .should().beFreeOfCycles()

    rule.check(classes)
  }

}