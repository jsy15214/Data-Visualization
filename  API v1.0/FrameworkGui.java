package hw5a;

/**
 * The framework GUI implementation. This class is responsible for displaying
 * the framework GUI to the screen, and for forwarding events to
 * {@link FrameworkImpl} when GUI-related events are detected.
 */
public class FrameworkGui implements StateChangeListener {

	/** core of framework */
	private FrameImpl core;

	public FrameworkGui(FrameImpl fc) {
		core = fc;
	}

	@Override
	public void onDataPluginRegistered(DataPlugin plugin) {

	}

	@Override
	public void onDisplayPluginRegistered(DisplayPlugin plugin) {

	}

	@Override
	public void onFileReady() {

	}

	@Override
	public void onDataExtracted() {

	}

	@Override
	public void onFunctionSelected() {

	}

	@Override
	public void onChartSelected() {

	}
}
