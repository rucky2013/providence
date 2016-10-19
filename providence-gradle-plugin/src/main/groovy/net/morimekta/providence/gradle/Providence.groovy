package net.morimekta.providence.gradle

/**
 * TODO(steineldar): Make a proper class description.
 */
class Providence implements Plugin<Project> {
    void apply(Project target) {
        project.extensions.create("providence", ProvidenceExtension)

        project.task("compile") << {
        }
    }
}
