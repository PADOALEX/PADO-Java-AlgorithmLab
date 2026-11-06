# Dependencies and license metadata

Source license: MIT, in `LICENSE`. This is a newly prepared Java implementation of standard data-structure and sorting ideas, with conceptual attribution to the owner's C# predecessor. It does not copy or redistribute that repository's C# source files.

There are no runtime libraries beyond the JDK. JUnit Jupiter 5.11.4 is test-only. The resolved tree is retained in `DEPENDENCY_TREE.txt`; Maven's license plugin generated `THIRD_PARTY_LICENSES.txt` for all eight direct/transitive test dependencies. JUnit uses EPL-2.0; API Guardian and OpenTest4J metadata list Apache-2.0. No unlabelled dependency was reported in the generated inventory.

The checked-in compiler, Surefire and JAR plugin versions resolve successfully. Maven itself and those build plugins are build tools, not application runtime dependencies. Temurin/OpenJDK has its own GPLv2 with Classpath Exception notices and is not bundled in the source repository. Maven uses Apache-2.0. The GitHub Actions workflow references official checkout/setup-java actions; remote execution remains pending.

The inventory records publisher POM metadata, not a legal certification or a vulnerability scan. Keep dependency notices when redistributing binaries. Review licenses again if adding libraries. The task's portable JDK/Maven archives and dependency cache are excluded from this repository and source packages.
