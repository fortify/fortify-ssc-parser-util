package com.fortify.ssc.parser.cyclonedx.parser;

import org.apache.commons.codec.digest.DigestUtils;

import com.fortify.plugin.api.BasicVulnerabilityBuilder.Priority;
import com.fortify.plugin.api.FortifyAnalyser;
import com.fortify.plugin.api.FortifyKingdom;
import com.fortify.plugin.api.StaticVulnerabilityBuilder;
import com.fortify.plugin.api.VulnerabilityHandler;
import com.fortify.ssc.parser.cyclonedx.CustomVulnAttribute;
import com.fortify.ssc.parser.cyclonedx.domain.Bom;
import com.fortify.ssc.parser.cyclonedx.domain.Component;
import com.fortify.ssc.parser.cyclonedx.domain.Vulnerability;
import com.fortify.ssc.parser.cyclonedx.domain.Vulnerability.ComponentReference;
import com.fortify.util.ssc.parser.EngineTypeHelper;
import com.fortify.util.ssc.parser.HandleDuplicateIdVulnerabilityHandler;

public final class VulnerabilitiesProducer {
	private final VulnerabilityHandler vulnerabilityHandler;
	
	/**
	 * Constructor for storing {@link VulnerabilityHandler} instance.
	  * @param vulnerabilityHandler
	 */
	public VulnerabilitiesProducer(final VulnerabilityHandler vulnerabilityHandler) {
		this.vulnerabilityHandler = new HandleDuplicateIdVulnerabilityHandler(vulnerabilityHandler);
	}
	
	/**
	 * This method produces a Fortify vulnerability based on the given
	 * {@link Bom} and {@link Vulnerability} instances.
	 */
	public final void produceVulnerabilities(Bom bom, Vulnerability vulnerability) {
		ComponentReference[] componentRefs = vulnerability.getAffects();
		for ( ComponentReference componentRef : componentRefs ) {
			produceVulnerability(bom, bom.getComponentByBomRef(componentRef.getRef()), vulnerability);
		}
	}
	
	@SuppressWarnings("deprecation") // SSC JavaDoc states that severity is mandatory, but method is deprecated
	public final void produceVulnerability(Bom bom, Component component, Vulnerability vulnerability) {
		Priority priority = vulnerability.getFortifyPriority();
		if ( priority != null ) {
			StaticVulnerabilityBuilder vb = vulnerabilityHandler.startStaticVulnerability(getInstanceId(component, vulnerability));
			
			// Set meta-data
			vb.setEngineType(EngineTypeHelper.getEngineType());
			
			vb.setKingdom(FortifyKingdom.ENVIRONMENT.getKingdomName());
			vb.setAnalyzer(FortifyAnalyser.CONFIGURATION.getAnalyserName());
			vb.setCategory("Insecure Deployment");
			vb.setSubCategory("Unpatched Application");
			
			// Set mandatory values to JavaDoc-recommended values
			vb.setAccuracy(5.0f);
			vb.setSeverity(2.5f);
			vb.setConfidence(2.5f);
			vb.setProbability(2.5f);
			vb.setImpact(2.5f);
			vb.setLikelihood(2.5f);
			
			// Set standard vulnerability fields based on input
			vb.setFileName(component.getPurlAsString());
			vb.setPriority(priority);
			//vb.setRuleGuid(getRuleGuid(runData, result));
			vb.setVulnerabilityAbstract(vulnerability.getDescription());
			
			//vb.setClassName(null);
    		//vb.setFunctionName(functionName);
    		//vb.setLineNumber(lineNumber);
    		//vb.setMappedCategory(mappedCategory);
    		//vb.setMinVirtualCallConfidence(minVirtualCallConfidence);
    		//vb.setPackageName(packageName);
    		//vb.setRemediationConstant(remediationConstant);
    		//vb.setRuleGuid(ruleGuid);
    		//vb.setSink(sink);
    		//vb.setSinkContext(sinkContext);
    		//vb.setSource(source);
    		//vb.setSourceContext(sourceContext);
    		//vb.setSourceFile(sourceFile);
    		//vb.setSourceLine(sourceLine);
    		//vb.setTaintFlag(taintFlag);
			//vb.setVulnerabilityRecommendation(vulnerability.getRecommendation()); // No idea how to display this, so we use custom attribute instead
			
			//vb.set*CustomAttributeValue(...)
			
			vb.setStringCustomAttributeValue(CustomVulnAttribute.externalId, vulnerability.getId());
			vb.setStringCustomAttributeValue(CustomVulnAttribute.externalUrl, vulnerability.getIssueUrl(bom));
			vb.setStringCustomAttributeValue(CustomVulnAttribute.cwes, vulnerability.getCwesAsString());
			vb.setStringCustomAttributeValue(CustomVulnAttribute.invoked, vulnerability.getInvokedAsString());
			vb.setStringCustomAttributeValue(CustomVulnAttribute.controllable, vulnerability.getControllableAsString());
			vb.setStringCustomAttributeValue(CustomVulnAttribute.evidenceUrl, vulnerability.getEvidenceUrl());
			vb.setStringCustomAttributeValue(CustomVulnAttribute.detail, vulnerability.getDetail());
			vb.setStringCustomAttributeValue(CustomVulnAttribute.recommendation, vulnerability.getRecommendation());
			
			vb.setStringCustomAttributeValue(CustomVulnAttribute.toolName, bom.getToolName());
			
			vb.setStringCustomAttributeValue(CustomVulnAttribute.componentPackageType, component.getPackageType());
			vb.setStringCustomAttributeValue(CustomVulnAttribute.componentNamespace, component.getGroup());
			vb.setStringCustomAttributeValue(CustomVulnAttribute.componentName, component.getName());
			vb.setStringCustomAttributeValue(CustomVulnAttribute.componentVersion, component.getVersion());
			vb.setStringCustomAttributeValue(CustomVulnAttribute.componentScope, component.getScopeName());
			vb.setStringCustomAttributeValue(CustomVulnAttribute.componentDescription, component.getDescription());
			vb.setStringCustomAttributeValue(CustomVulnAttribute.componentPurl, component.getPurlAsString());
			vb.setStringCustomAttributeValue(CustomVulnAttribute.componentLicenses, component.getLicensesAsString());
    		
    		vb.completeVulnerability();
		}
	}
	
	private String getInstanceId(Component component, Vulnerability vulnerability) {
		return DigestUtils.sha256Hex(getInstanceIdString(component, vulnerability));
	}
	
	private String getInstanceIdString(Component component, Vulnerability vulnerability) {
		return component.getBomRef() + ":" + vulnerability.getId();
	}
}
