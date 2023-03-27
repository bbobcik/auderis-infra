# PROJECT SECURITY


## Software Bill of Material (SBOM)

See <https://openssf.org> for information why support of SBOM is becoming
critical:

> An SBOM is a list of the software components in a software system. It is
> maintained as an inventory list that enables developers and organizations to
> effectively and efficiently evaluate risk management use cases such as
> vulnerability analysis. There are emerging SBOM use cases including software
> supportability, incident investigation, and run-time protection.

Support for a creation of SBOMs is provided within the project. Once an
appropriate plugin (`org.cyclonedx.bom`) is applied to a project module,
initialization steps are automatically performed.

Invoke the `cyclonedxBom` task to produce SBOM for the module.