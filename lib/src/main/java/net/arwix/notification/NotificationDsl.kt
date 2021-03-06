@file:Suppress("unused", "MemberVisibilityCanBePrivate", "NOTHING_TO_INLINE", "TooManyFunctions")

package net.arwix.notification

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.RemoteViews
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.IntRange
import androidx.core.app.NotificationCompat
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime

@NotificationMarker
class NotificationDsl @PublishedApi internal constructor(
    @PublishedApi internal val builder: NotificationCompat.Builder,
    internal val context: Context
) {

    /**
     * Notification's actions
     */
    inline val actions: Actions
        get() = Actions(builder)

    /**
     * Notification's actions
     */
    inline fun actions(body: @NotificationMarker Actions.() -> Unit) {
        actions.body()
    }

    /**
     * Determines whether the platform can generate contextual actions for a notification.
     *
     * By default this is true.
     */
    fun allowSystemGeneratedContextualActions(allowed: Boolean) {
        builder.setAllowSystemGeneratedContextualActions(allowed)
    }

    /**
     * Setting this flag will make it so the notification is automatically canceled
     * when the user clicks it in the panel.
     *
     * The PendingIntent set with [deleteIntent] will be broadcast when the notification is canceled.
     */
    fun autoCancel(autoCancel: Boolean) {
        builder.setAutoCancel(autoCancel)
    }

    /**
     * Sets which icon to display as a badge for this notification.
     *
     * Must be one of [BADGE_ICON_NONE][NotificationCompat.BADGE_ICON_NONE],
     * [BADGE_ICON_SMALL][NotificationCompat.BADGE_ICON_SMALL], [BADGE_ICON_LARGE][NotificationCompat.BADGE_ICON_LARGE].
     *
     * **Note:** This value might be ignored, for launchers that don't support badge icons.
     */
    fun badgeIconType(@NotificationCompat.BadgeIconType value: Int) {
        builder.setBadgeIconType(value)
    }

    /**
     * Set the notification category.
     *
     * Must be one of the predefined notification categories (see the `CATEGORY_*`
     * constants in [NotificationCompat]) that best describes this notification.
     *
     * May be used by the system for ranking and filtering.
     */
    fun category(@NotificationCategory category: String) {
        builder.setCategory(category)
    }

    /**
     * Sets [Notification.color].
     *
     * @param color The accent color to use
     */
    fun color(@ColorInt color: Int) {
        builder.color = color
    }

    /**
     * Set whether this notification should be colorized. When set, the color set with [color] will be used
     * as the background color of this notification.
     *
     * This should only be used for high priority ongoing tasks like navigation, an ongoing call,
     * or other similarly high-priority events for the user.
     *
     * For most styles, the coloring will only be applied if the notification is for a
     * foreground service notification.
     *
     * However, for MediaStyle and DecoratedMediaCustomViewStyle notifications that have a media session attached
     * there is no such requirement.
     *
     * Calling this method on any version prior to [Build.VERSION_CODES.O][android.os.Build.VERSION_CODES.O] will
     * not have an effect on the notification and it won't be colorized.
     *
     * @see color
     */
    fun colorized(colorize: Boolean) {
        builder.setColorized(colorize)
    }

    /**
     * Supply a custom [RemoteViews] to use instead of the standard one.
     */
    fun content(views: RemoteViews) {
        builder.setContent(views)
    }

    /**
     * Supply a [PendingIntent] to send when the notification is clicked. If you do not supply an intent,
     * you can now add [PendingIntent]s to individual views to be launched when clicked by calling
     * [RemoteViews.setOnClickPendingIntent].
     * Be sure to read [Notification.contentIntent] for how to correctly use this.
     */
    fun contentIntent(intent: PendingIntent) {
        builder.setContentIntent(intent)
    }

    /**
     * Set the large text at the right-hand side of the notification.
     */
    fun contentInfo(info: CharSequence) {
        builder.setContentInfo(info)
    }

    /**
     * Set the text (second row) of the notification, in a standard notification.
     */
    fun contentText(text: CharSequence) {
        builder.setContentText(text)
    }

    /**
     * Set the title (first row) of the notification, in a standard notification.
     */
    fun contentTitle(title: CharSequence) {
        builder.setContentTitle(title)
    }

    /**
     * Supply custom RemoteViews to use instead of the platform template in the heads up dialog.
     *
     * This will override the heads-up layout that would otherwise be constructed by this Builder object.
     *
     * No-op on versions prior to [Build.VERSION_CODES.LOLLIPOP][android.os.Build.VERSION_CODES.LOLLIPOP].
     */
    fun customHeadsUpContentView(contentView: RemoteViews) {
        builder.setCustomHeadsUpContentView(contentView)
    }

    /**
     * Set the default notification options that will be used.
     *
     * The value should be one or more of the following fields combined with bitwise-or:
     * [NotificationCompat.DEFAULT_SOUND], [NotificationCompat.DEFAULT_VIBRATE], [NotificationCompat.DEFAULT_LIGHTS].
     *
     * For all default values, use [NotificationCompat.DEFAULT_ALL].
     */
    fun defaults(@NotificationDefaults defaults: Int) {
        builder.setDefaults(defaults)
    }

    /**
     * Supply a [PendingIntent] to send when the notification is cleared by the user directly from
     * the notification panel. For example, this intent is sent when the user clicks the "Clear all" button,
     * or the individual "X" buttons on notifications. This intent is not sent when the application calls
     * [NotificationManager.cancel].
     */
    fun deleteIntent(intent: PendingIntent) {
        builder.setDeleteIntent(intent)
    }

    /**
     * The current metadata [Bundle] used by this notification Builder.
     */
    inline var extras: Bundle
        get() = builder.extras
        set(value) {
            builder.setExtras(value)
        }

    /**
     * Apply an extender to this notification builder.
     * Extenders may be used to add metadata or change options on this builder.
     */
    fun extend(extender: NotificationCompat.Extender) {
        builder.extend(extender)
    }

    /**
     * An intent to launch instead of posting the notification to the status bar.
     * Only for use with extremely high-priority notifications demanding the user's **immediate** attention,
     * such as an incoming phone call or alarm clock that the user has explicitly set to a particular time.
     * If this facility is used for something else, please give the user an option
     * to turn it off and use a normal notification, as this can be extremely disruptive.
     *
     * &nbsp;
     *
     * On some platforms, the system UI may choose to display a heads-up notification,
     * instead of launching this intent, while the user is using the device.
     *
     * &nbsp;
     *
     * @param intent The pending intent to launch.
     * @param highPriority Passing true will cause this notification to be sent even
     * if other notifications are suppressed.
     */
    fun fullScreenIntent(intent: PendingIntent, highPriority: Boolean = false) {
        builder.setFullScreenIntent(intent, highPriority)
    }

    /**
     * Set this notification to be part of a group of notifications sharing the same key.
     * Grouped notifications may display in a cluster or stack on devices which support such rendering.
     *
     * To make this notification the summary for its group, also call [groupSummary].
     * A sort order can be specified for group members by using [sortKey]
     *
     * @param groupKey The group key of the group.
     * @return this object for method chaining
     *
     * @see groupSummary
     */
    fun group(groupKey: String) {
        builder.setGroup(groupKey)
    }

    /**
     * Sets the group alert behavior for this notification. Use this method to mute this
     * notification if alerts for this notification's group should be handled by a different
     * notification. This is only applicable for notifications that belong to a
     * [group]. This must be called on all notifications you want to mute.
     *
     * For example, if you want only the summary of your group to make noise, all children in the group should have
     * the group alert behavior [GROUP_ALERT_SUMMARY][NotificationCompat.GROUP_ALERT_SUMMARY].
     *
     * &nbsp;
     *
     * The default value is [GROUP_ALERT_ALL][NotificationCompat.GROUP_ALERT_ALL]
     */
    fun groupAlertBehavior(@NotificationCompat.GroupAlertBehavior groupAlertBehavior: Int) {
        builder.setGroupAlertBehavior(groupAlertBehavior)
    }

    /**
     * Set this notification to be the group summary for a group of notifications.
     * Grouped notifications may display in a cluster or stack on devices which
     * support such rendering. Requires a group key also be set using [group].
     *
     * @param isGroupSummary Whether this notification should be a group summary.
     *
     * @see group
     */
    fun groupSummary(isGroupSummary: Boolean) {
        builder.setGroupSummary(isGroupSummary)
    }

    /**
     * Set the large icon that is shown in the ticker and notification.
     */
    fun largeIcon(icon: Bitmap) {
        builder.setLargeIcon(icon)
    }

    /**
     * Set the argb value that you would like the LED on the device to blink, as well as the
     * rate. The rate is specified in terms of the number of milliseconds to be on
     * and then the number of milliseconds to be off.
     */
    fun lights(
        @ColorInt color: Int,
        @IntRange(from = 0) onMs: Int,
        @IntRange(from = 0) offMs: Int
    ) {
        builder.setLights(color, onMs, offMs)
    }

    /**
     * Set whether or not this notification is only relevant to the current device.
     *
     * Some notifications can be bridged to other devices for remote display.
     * This hint can be set to recommend this notification not be bridged.
     */
    fun localOnly(localOnly: Boolean) {
        builder.setLocalOnly(localOnly)
    }

    /**
     * Set the large number at the right-hand side of the notification. This is equivalent to setContentInfo,
     * although it might show the number in a different font size for readability.
     */
    fun number(number: Int) {
        builder.setNumber(number)
    }

    /**
     * Set whether this is an ongoing notification.
     *
     * Ongoing notifications differ from regular notifications in the following ways:
     * * Ongoing notifications are sorted above the regular notifications in the notification panel.
     * * Ongoing notifications do not have an 'X' close button, and are not affected by the "Clear all" button.
     */
    fun ongoing(ongoing: Boolean) {
        builder.setOngoing(ongoing)
    }

    /**
     * Set this flag if you would only like the sound, vibrate
     * and ticker to be played if the notification is not already showing.
     */
    fun onlyAlertOnce(onlyAlertOnce: Boolean) {
        builder.setOnlyAlertOnce(onlyAlertOnce)
    }

//    /**
//     * Persons associated with the notification
//     */
//    inline val persons: Persons
//        get() = Persons(notification)

//    /**
//     * Managing persons associated with the notification
//     */
//    inline fun persons(body: @NotificationMarker Persons.() -> Unit) {
//        persons.body()
//    }
    /**
     * Set the relative priority for this notification.
     *
     * Priority is an indication of how much of the user's valuable attention should be consumed by this
     * notification. Low-priority notifications may be hidden from the user in certain situations,
     * while the user might be interrupted for a higher-priority notification.
     * The system sets a notification's priority based on various factors including the setPriority value.
     * The effect may differ slightly on different platforms.
     *
     * @param priority Relative priority for this notification.
     * Must be one of the priority constants defined by [NotificationCompat].
     * Acceptable values range from [NotificationCompat.PRIORITY_MIN] (-2) to [NotificationCompat.PRIORITY_MAX] (2).
     */
    fun priority(@NotificationPriority priority: Int) {
        builder.priority = priority
    }

    /**
     * Set the progress this notification represents, which may be
     * represented as a [ProgressBar][android.widget.ProgressBar].
     */
    fun progress(
        @IntRange(from = 0) max: Int,
        @IntRange(from = 0) progress: Int,
        indeterminate: Boolean = false
    ) {
        builder.setProgress(max, progress, indeterminate)
    }

    /**
     * Supply a replacement Notification whose contents should be shown in insecure contexts
     * (i.e. atop the secure lockscreen). See [Notification.visibility] and
     * [VISIBILITY_PUBLIC][NotificationCompat.VISIBILITY_PUBLIC].
     *
     * @param notification A replacement notification, presumably with some or all info redacted.
     */
    infix fun publicVersion(notification: Notification) {
        this.builder.setPublicVersion(notification)
    }

    /**
     * Set the remote input history.
     *
     * This should be set to the most recent inputs that have been sent
     * through a [RemoteInput][androidx.core.app.RemoteInput] of this Notification and cleared once the it is no
     * longer relevant (e.g. for chat notifications once the other party has responded).
     *
     * The most recent input must be stored at the 0 index, the second most recent at the
     * 1 index, etc. Note that the system will limit both how far back the inputs will be shown
     * and how much of each individual input is shown.
     *
     * **Note:** The reply text will only be shown on notifications that have least one action with a `RemoteInput`.
     */
    fun remoteInputHistory(text: Array<CharSequence>) {
        builder.setRemoteInputHistory(text)
    }

    /**
     * If this notification is duplicative of a Launcher shortcut,
     * sets the [id][androidx.core.content.pm.ShortcutInfoCompat.getId] of the shortcut,
     * in case the Launcher wants to hide the shortcut.
     *
     * **Note:** This field will be ignored by Launchers that don't support
     * badging or [shortcuts][androidx.core.content.pm.ShortcutInfoCompat].
     *
     * @param shortcutId the [id][androidx.core.content.pm.ShortcutInfoCompat.getId]
     * of the shortcut this notification supersedes
     */
    fun shortcutId(shortcutId: String) {
        builder.setShortcutId(shortcutId)
    }

    /**
     * Control whether the timestamp set with [whenTime] is shown in the content view.
     */
    fun showWhen(show: Boolean) {
        builder.setShowWhen(show)
    }

    /**
     * Silences this instance of the notification, regardless of the sounds or vibrations set
     * on the notification or notification channel.
     */
    fun silent() {
        builder.setNotificationSilent()
    }

    /**
     * Set the small icon to use in the notification layouts. Different classes of devices
     * may return different sizes. See the UX guidelines for more information on how to
     * design these icons.
     *
     * @param icon A resource ID in the application's package of the drawable to use.
     */
    fun smallIcon(@DrawableRes icon: Int) {
        builder.setSmallIcon(icon)
    }

    /**
     * A variant of [smallIcon] that takes an additional level parameter for when the icon is a
     * [LevelListDrawable][android.graphics.drawable.LevelListDrawable].
     *
     * @param icon A resource ID in the application's package of the drawable to use.
     * @param level The level to use for the icon.
     *
     * @see android.graphics.drawable.LevelListDrawable
     */
    fun smallIcon(@DrawableRes icon: Int, @IntRange(from = 0) level: Int) {
        builder.setSmallIcon(icon, level)
    }

    /**
     * Set a sort key that orders this notification among other notifications from the
     * same package. This can be useful if an external sort was already applied and an app
     * would like to preserve this. Notifications will be sorted lexicographically using this
     * value, although providing different priorities in addition to providing sort key may
     * cause this value to be ignored.
     *
     * This sort key can also be used to order members of a notification group. See [group]
     *
     * @see String.compareTo
     */
    fun sortKey(sortKey: String) {
        builder.setSortKey(sortKey)
    }

    /**
     * Set the sound to play. It will play on the default stream.
     *
     * On some platforms, a notification that is noisy is more likely to be presented as a heads-up notification.
     */
    fun sound(sound: Uri) {
        builder.setSound(sound)
    }

    /**
     * Set the sound to play. It will play on the stream you supply.
     *
     * On some platforms, a notification that is noisy is more likely to be presented as a heads-up notification.
     *
     * See [AudioManager][android.media.AudioManager] for the `STREAM_` constants.
     *
     * @see NotificationCompat.STREAM_DEFAULT
     */
    fun sound(sound: Uri, @NotificationCompat.StreamType streamType: Int) {
        builder.setSound(sound, streamType)
    }

    /**
     * Add a rich notification style to be applied at build time.
     *
     * If the platform does not provide rich notification styles, this method has no effect. The
     * user will always see the normal notification style.
     *
     * @param style Object responsible for modifying the notification style.
     */
    inline fun Notification.style(style: NotificationCompat.Style) {
        builder.setStyle(style)
    }

    /**
     * Set the third line of text in the platform notification template.
     * Don't use if you're also using [progress]; they occupy the same location in the standard template.
     *
     * &nbsp;
     *
     * If the platform does not provide large-format notifications, this method has no effect.
     * The third line of text only appears in expanded view.
     */
    fun subText(text: CharSequence) {
        builder.setSubText(text)
    }

    /**
     * Sets the "ticker" text which is sent to accessibility services. Prior to [Build.VERSION_CODES.LOLLIPOP],
     * sets the text that is displayed in the status bar when the notification first arrives,
     * and also a RemoteViews object that may be displayed instead on some devices.
     */
    fun ticker(tickerText: CharSequence, views: RemoteViews? = null) {
        builder.setTicker(tickerText, views)
    }

    /**
     * Specifies the time at which this notification should be canceled, if it is not already canceled.
     */
    fun timeoutAfter(@IntRange(from = 0) durationMs: Long) {
        builder.setTimeoutAfter(durationMs)
    }

    /**
     * Show the [Notification.when][Notification.when] field as a stopwatch.
     *
     * Instead of presenting `when` as a timestamp, the notification will show an
     * automatically updating display of the minutes and seconds since `when`.
     *
     * Useful when showing an elapsed time (like an ongoing phone call).
     *
     * @see android.widget.Chronometer
     * @see Notification.when
     */
    fun usesChronometer(value: Boolean) {
        builder.setUsesChronometer(value)
    }

    /**
     * Set the vibration pattern to use.
     *
     * On some platforms, a notification that vibrates is more likely to be presented as a heads-up notification.
     *
     * See [Vibrator][android.os.Vibrator] for a discussion of the `pattern` parameter.
     */
    fun vibrate(pattern: LongArray) {
        builder.setVibrate(pattern)
    }

    /**
     * Sets [Notification.visibility].
     *
     * @param visibility One of [NotificationCompat.VISIBILITY_PRIVATE] (the default),
     * [NotificationCompat.VISIBILITY_PUBLIC], or [NotificationCompat.VISIBILITY_SECRET].
     */
    fun visibility(@NotificationVisibility visibility: Int) {
        builder.setVisibility(visibility)
    }

    /**
     * Set the time that the event occurred. Notifications in the panel are sorted by this time.
     */
    fun whenTime(@IntRange(from = 0) time: Long) {
        builder.setWhen(time)
    }

    /**
     * Set the argb value that you would like the LED on the device to blink, as well as the
     * rate. The rate is specified in terms of the number of milliseconds to be on
     * and then the number of milliseconds to be off.
     */
    @ExperimentalTime
    inline fun Notification.lights(@ColorInt color: Int, on: Duration, off: Duration) {
        require(on.isPositive() && on.isFinite()) { "`on` must be greater or equals than zero and finite" }
        require(off.isPositive() && off.isFinite()) { "`off` must be greater or equals than zero and finite" }
        lights(color, on.toInt(DurationUnit.MILLISECONDS), off.toInt(DurationUnit.MILLISECONDS))
    }


}