package dev.danvega.bookstore;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.data.repository.Repository;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * AGENTS.md asks for package by feature. These rules check it on every build.
 */
@AnalyzeClasses(packages = "dev.danvega.bookstore", importOptions = ImportOption.DoNotIncludeTests.class)
class PackageByFeatureTest {

    @ArchTest
    static final ArchRule no_layer_packages = noClasses()
            .should().resideInAnyPackage("..controller..", "..service..", "..repository..", "..model..")
            .because("AGENTS.md says package by feature, not by layer");

    @ArchTest
    static final ArchRule repositories_stay_in_their_feature = classes()
            .that().areAssignableTo(Repository.class)
            .should().notBePublic()
            .because("AGENTS.md says other features use the public service, never the repository");
}
