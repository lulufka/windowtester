package abbot.editor.recorder;

/**
 * Provide mnemonic constants for different types of semantic events.
 */
public interface SemanticEvents {
    /**
     * No event recognized.
     */
    int SE_NONE = -1;
    /**
     * Any event recognized.
     */
    int SE_ANY = 0;
    /**
     * Window show/hide/move/resize/activate.
     */
    int SE_WINDOW = 1;
    /**
     * Standard or popup menu
     */
    int SE_MENU = 2;
    /**
     * Mouse down/up.
     */
    int SE_CLICK = 3;
    /**
     * Key typed events.
     */
    int SE_KEY = 4;
    /**
     * Generic drag event.  Usually invoked from another handler.
     */
    int SE_DRAG = 5;
    /**
     * Generic drop event.  Wait for drag to terminate.
     */
    int SE_DROP = 6;
    /**
     * Text input (multiple keystrokes).
     */
    int SE_TEXT = 7;
    /**
     * Input method input (extended character input).
     */
    int SE_IM = 8;
}
