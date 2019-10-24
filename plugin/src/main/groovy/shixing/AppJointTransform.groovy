package shixing

import com.android.build.api.transform.*
import com.google.common.collect.Sets
import org.gradle.api.Project

class AppJointTransform extends Transform {

    private Project mProject

    AppJointTransform(Project project) {
        mProject = project
    }

    @Override
    String getName() {
        return "appJoint"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return Collections.singleton(QualifiedContent.DefaultContentType.CLASSES)
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return Sets.immutableEnumSet(
                QualifiedContent.Scope.PROJECT,
                QualifiedContent.Scope.SUB_PROJECTS,
                QualifiedContent.Scope.EXTERNAL_LIBRARIES
        )
    }

    @Override
    boolean isIncremental() {
        return false
    }
}