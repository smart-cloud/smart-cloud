package org.smartframework.cloud.starter.log.log4j2.plugin;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.PatternConverter;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.smartframework.cloud.mask.util.MaskUtil;

@Plugin(name = "MaskMessagePatternConverter", category = PatternConverter.CATEGORY)
// mm: mask message
@ConverterKeys({ "mm"})
@PerformanceSensitive("allocation")
public class MaskMessagePatternConverter extends LogEventPatternConverter {

    /**
     * Private constructor.
     *
     * @param options
     *            options, may be null.
     */
    private MaskMessagePatternConverter(final Configuration config, final String[] options) {
        super("Mmessage", "mmessage");
    }

    /**
     * Obtains an instance of pattern converter.
     *
     * @param config
     *            The Configuration.
     * @param options
     *            options, may be null.
     * @return instance of pattern converter.
     */
    public static MaskMessagePatternConverter newInstance(final Configuration config, final String[] options) {
        return new MaskMessagePatternConverter(config, options);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        Message message = event.getMessage();
        // 参数脱敏处理
    	Object[] params = message.getParameters();
    	if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				params[i] = MaskUtil.mask(params[i]);
			}
		}
    	toAppendTo.append(ParameterizedMessage.format(message.getFormat(), params));
    }
}
