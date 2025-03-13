package com.enote.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;

import com.enote.util.CommonUtil;

public class AuditAwareConfig implements AuditorAware<Integer>{
	
	@Autowired
	private CommonUtil commonUtil;

	@Override
	public Optional<Integer> getCurrentAuditor() {
		return Optional.of(commonUtil.getLoggingUser().getId());
	}

}
