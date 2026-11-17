package dev.danvega.bookstore;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.GeneralCodingRules.DEPRECATED_API_SHOULD_NOT_BE_USED;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;

/**
 * Most of these rules ship with ArchUnit in GeneralCodingRules. Only the Jackson rule is written here.
 */
@AnalyzeClasses(packages = "dev.danvega.bookstore", importOptions = ImportOption.DoNotIncludeTests.class)
class CodingRulesTest {

    @ArchTest
    static final ArchRule no_field_injection = NO_CLASSES_SHOULD_USE_FIELD_INJECTION;

    @ArchTest
    static final ArchRule no_system_out = NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;

    @ArchTest
    static final ArchRule no_java_util_logging = NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;

    @ArchTest
    static final ArchRule no_generic_exceptions = NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS;

    @ArchTest
    static final ArchRule no_deprecated_api = DEPRECATED_API_SHOULD_NOT_BE_USED;

    // Jackson 3 keeps the annotations in com.fasterxml.jackson.annotation, so only core and databind are banned.
    @ArchTest
    static final ArchRule jackson_3_only = noClasses()
            .should().dependOnClassesThat().resideInAnyPackage(
                    "com.fasterxml.jackson.core..", "com.fasterxml.jackson.databind..")
            .because("AGENTS.md says Spring Boot 4 uses Jackson 3 (tools.jackson)");
}
