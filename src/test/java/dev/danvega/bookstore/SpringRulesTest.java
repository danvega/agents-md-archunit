package dev.danvega.bookstore;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noMethods;

/**
 * AGENTS.md asks controllers to stay thin. These rules check it on every build.
 * Controller.class also matches @RestController, because @RestController is meta-annotated with it.
 */
@AnalyzeClasses(packages = "dev.danvega.bookstore", importOptions = ImportOption.DoNotIncludeTests.class)
class SpringRulesTest {

    @ArchTest
    static final ArchRule controllers_do_not_use_repositories = noClasses()
            .that().areMetaAnnotatedWith(Controller.class)
            .should().dependOnClassesThat().areAssignableTo(Repository.class)
            .because("AGENTS.md says a controller calls its feature's service, never a repository");

    @ArchTest
    static final ArchRule controllers_are_not_transactional = noClasses()
            .that().areMetaAnnotatedWith(Controller.class)
            .should().beAnnotatedWith(Transactional.class)
            .because("AGENTS.md says transactions belong in the service");

    @ArchTest
    static final ArchRule controller_methods_are_not_transactional = noMethods()
            .that().areDeclaredInClassesThat().areMetaAnnotatedWith(Controller.class)
            .should().beAnnotatedWith(Transactional.class)
            .because("AGENTS.md says transactions belong in the service");
}
