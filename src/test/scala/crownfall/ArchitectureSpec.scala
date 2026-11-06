package crownfall

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.{ClassFileImporter, ImportOption}
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ArchitectureSpec extends AnyWordSpec with Matchers:

  // Only production code is checked. Tests may use helpers across layers.
  private val classes: JavaClasses =
    ClassFileImporter()
      .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
      .importPackages("crownfall")

  "The model" should {

    "not depend on the controller" in {
      noClasses()
        .that().resideInAPackage("crownfall.model..")
        .should().dependOnClassesThat().resideInAPackage("crownfall.controller..")
        .check(classes)
    }

    "not depend on the view" in {
      noClasses()
        .that().resideInAPackage("crownfall.model..")
        .should().dependOnClassesThat().resideInAPackage("crownfall.aview..")
        .check(classes)
    }

    "not depend on util" in {
      noClasses()
        .that().resideInAPackage("crownfall.model..")
        .should().dependOnClassesThat().resideInAPackage("crownfall.util..")
        .check(classes)
    }
  }

  "The controller" should {

    "not depend on the view" in {
      noClasses()
        .that().resideInAPackage("crownfall.controller..")
        .should().dependOnClassesThat().resideInAPackage("crownfall.aview..")
        .check(classes)
    }
  }

  "The util package" should {

    "not depend on any other layer" in {
      noClasses()
        .that().resideInAPackage("crownfall.util..")
        .should().dependOnClassesThat()
        .resideInAnyPackage("crownfall.model..", "crownfall.controller..", "crownfall.aview..")
        .check(classes)
    }
  }

  "The packages" should {

    "be free of cycles" in {
      slices()
        .matching("crownfall.(*)..")
        .should().beFreeOfCycles()
        .check(classes)
    }
  }