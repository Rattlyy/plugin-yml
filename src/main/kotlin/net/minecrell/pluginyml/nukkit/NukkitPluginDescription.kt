/*
 *     SPDX-License-Identifier: MIT
 *
 *     Copyright (c) 2017 Minecrell <https://github.com/Minecrell>
 *     Copyright (c) 2024 EldoriaRPG and Contributor <https://github.com/eldoriarpg>
 */

package net.minecrell.pluginyml.nukkit

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.lang.Closure
import net.minecrell.pluginyml.PluginDescription
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.Optional

class NukkitPluginDescription(project: Project) : PluginDescription() {

    @Input var name: String? = null
    @Input var main: String? = null
    @Input var version: String? = null
    @Input var api: List<String>? = null
    @Input @Optional var description: String? = null
    @Input @Optional var load: PluginLoadOrder? = null
    @Input @Optional var author: String? = null
    @Input @Optional var authors: List<String>? = null
    @Input @Optional var website: String? = null
    @Input @Optional var depend: List<String>? = null
    @Input @Optional @JsonProperty("softdepend") var softDepend: List<String>? = null
    @Input @Optional @JsonProperty("loadbefore") var loadBefore: List<String>? = null
    @Input @Optional var prefix: String? = null

    @Nested val commands: NamedDomainObjectContainer<Command> = project.container(Command::class.java)
    @Nested val permissions: NamedDomainObjectContainer<Permission> = project.container(Permission::class.java)

    // For Groovy DSL
    fun commands(closure: Closure<Unit>) = commands.configure(closure)
    fun permissions(closure: Closure<Unit>) = permissions.configure(closure)

    enum class PluginLoadOrder {
        STARTUP,
        POSTWORLD
    }

    data class Command(@Input @JsonIgnore val name: String) {
        @Input @Optional var description: String? = null
        @Input @Optional var aliases: List<String>? = null
        @Input @Optional var permission: String? = null
        @Input @Optional @JsonProperty("permission-message") var permissionMessage: String? = null
        @Input @Optional var usage: String? = null
    }

    class Permission(@Input @JsonIgnore val name: String, project: Project) {

        @Input @Optional var description: String? = null
        @Input @Optional var default: Default? = null

        @Nested val children: NamedDomainObjectContainer<Permission> = project.container(Permission::class.java)

        // For Groovy DSL
        fun children(closure: Closure<Unit>) = children.configure(closure)

        enum class Default {
            @JsonProperty("true")   TRUE,
            @JsonProperty("false")  FALSE,
            @JsonProperty("op")     OP,
            @JsonProperty("!op")    NOT_OP
        }
    }

}
