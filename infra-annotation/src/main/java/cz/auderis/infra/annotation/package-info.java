/**
 * Annotations in this package describe program element stereotypes with respect
 * to their relation to external environment.
 * <p>
 * They are primarily intended to document expected behavior of the annotated
 * components, but other libraries may use them for more advanced purposes:
 * <ul>
 *     <li>
 *         Dependency injection frameworks, such as CDI or Spring, may define
 *         these annotations as injection qualifiers or candidates for interception;
 *     </li>
 *     <li>
 *         Build-time or runtime code instrumentation may use the annotations
 *         to generate and validate performance metrics. They may for example
 *         decorate annotated methods with Java Flight Recorder event generation.
 *     </li>
 *     <li>
 *         Code verification frameworks (e.g. ArchUnit) may use the annotations
 *         to assert appropriate architectural constraints;
 *     </li>
 *     <li>
 *         Specialized tools may use the annotations to generate documentation.
 *         E.g. they may draw a graph of application components of various types,
 *         assemble tables of API endpoints, etc.
 * </ul>
 *
 * @author Boleslav Bobcik
 * @since 2023
 */
package cz.auderis.infra.annotation;
