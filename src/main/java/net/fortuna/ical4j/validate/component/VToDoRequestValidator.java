package net.fortuna.ical4j.validate.component;

import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VAlarm;
import net.fortuna.ical4j.model.component.VToDo;
import net.fortuna.ical4j.model.property.Method;
import net.fortuna.ical4j.validate.PropertyValidator;
import net.fortuna.ical4j.validate.ValidationException;
import net.fortuna.ical4j.validate.Validator;

import java.util.Arrays;

/**
 * <pre>
 * Component/Property  Presence
 * ------------------- ----------------------------------------------
 * METHOD                1       MUST be "REQUEST"
 * VTODO                 1+      All components must have the same UID
 *     ATTENDEE          1+
 *     DTSTAMP           1
 *     DTSTART           1
 *     ORGANIZER         1
 *     PRIORITY          1
 *     SEQUENCE          0 or 1  MUST be present if value is greater than
 *                               0, MAY be present if 0
 *     SUMMARY           1       Can be null.
 *     UID               1
 *
 *     ATTACH            0+
 *     CATEGORIES        0 or 1   This property may contain a list of
 *                                values
 *     CLASS             0 or 1
 *     COMMENT           0 or 1
 *     CONTACT           0+
 *     CREATED           0 or 1
 *     DESCRIPTION       0 or 1  Can be null
 *     DUE               0 or 1  If present DURATION MUST NOT be present
 *     DURATION          0 or 1  If present DUE MUST NOT be present
 *     EXDATE            0+
 *     EXRULE            0+
 *     GEO               0 or 1
 *     LAST-MODIFIED     0 or 1
 *     LOCATION          0 or 1
 *     PERCENT-COMPLETE  0 or 1
 *     RDATE             0+
 *     RECURRENCE-ID     0 or 1  present if referring to an instance of a
 *                               recurring calendar component.  Otherwise
 *                               it MUST NOT be present.
 *     RELATED-TO        0+
 *     RESOURCES         0 or 1  This property may contain a list of
 *                               values
 *     RRULE             0+
 *     STATUS            0 or 1  MAY be one of COMPLETED/NEEDS ACTION/IN-
 *                               PROCESS
 *     URL               0 or 1
 *     X-PROPERTY        0+
 *
 *     REQUEST-STATUS    0
 *
 * VALARM                0+
 *
 * VTIMEZONE             0+  MUST be present if any date/time refers
 *                           to a timezone
 * X-COMPONENT           0+
 *
 * VEVENT                0
 * VFREEBUSY             0
 * VJOURNAL              0
 * </pre>
 *
 */
public class VToDoRequestValidator implements Validator<VToDo> {

    private static final long serialVersionUID = 1L;

    public void validate(final VToDo target) throws ValidationException {
        PropertyValidator.getInstance().assertOneOrMore(Property.ATTENDEE, target.getProperties());

        Arrays.asList(Property.DTSTAMP, Property.DTSTART, Property.ORGANIZER, Property.PRIORITY, Property.SUMMARY, Property.UID).forEach(
                property -> PropertyValidator.getInstance().assertOne(property, target.getProperties()));

        Arrays.asList(Property.SEQUENCE, Property.CATEGORIES, Property.CLASS, Property.CREATED,
                Property.DESCRIPTION, Property.DUE, Property.DURATION, Property.GEO, Property.LAST_MODIFIED, Property.LOCATION,
                Property.PERCENT_COMPLETE, Property.RECURRENCE_ID, Property.RESOURCES, Property.STATUS, Property.URL).forEach(property -> PropertyValidator.getInstance().assertOneOrLess(property, target.getProperties()));

        PropertyValidator.getInstance().assertNone(Property.REQUEST_STATUS, target.getProperties());

        for (final VAlarm alarm : target.getAlarms()) {
            alarm.validate(Method.REQUEST);
        }
    }
}
