package com.fortify.ssc.parser.cyclonedx.domain;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fortify.util.mapdb.CustomSerializerElsa;

import lombok.Getter;

@Getter
public class PackageUrl implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final CustomSerializerElsa<PackageUrl> SERIALIZER = new CustomSerializerElsa<>(PackageUrl.class);
	@JsonIgnore private String purl;
	@JsonIgnore private String packageType;
	@JsonIgnore private String group;
	@JsonIgnore private String name;
	@JsonIgnore private String version;
	private static final Pattern purlPattern = Pattern.compile("^pkg:(?<type>[^/]+)(/(?<group>[^/]+))?(/(?<name>[^/?#@]+))(@(?<version>[^/?#@]+))?(.*)$");
	public PackageUrl(String purl) {
		this.purl = purl;
		Matcher matcher = purlPattern.matcher(purl);
		if ( matcher.matches() ) {
			this.packageType = matcher.group("type");
			this.group = matcher.group("group");
			this.name = matcher.group("name");
			this.version = matcher.group("version");
		}
	}
}