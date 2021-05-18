# skotliton

This is a simple sketelon for my Kotlin projects that have:
- **Code quality tools**: Ktlint, Detekt, Ben Mane's version plugin
- **Documentation**: Dokka
- **Uberjar**: Shadow
- **Publishing**: Bintray/jfrog
- **Testing**: Junit5
- **Serialization**: KotlinX serialization (commented by default)
- **Git Hook**: pre-push: run a gradle check before pushing to *main* branch

Considerations for later:
- **Building**: Batect

## Git Hooks

The Git Hook is not installed by itself, you will have to copy *qc/hooks/pre-push* to *.git/hooks/pre-push*
