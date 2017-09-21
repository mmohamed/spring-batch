package com.capgemini.validator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;

import com.mysql.jdbc.StringUtils;

public class FileNameParameterValidator implements JobParametersValidator {

    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        if (StringUtils.isEmptyOrWhitespaceOnly(parameters.getString("filename"))) {
            throw new JobParametersInvalidException("filename parameter required !");
        }
    }
}
