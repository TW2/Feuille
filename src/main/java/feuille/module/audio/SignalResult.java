package feuille.module.audio;

public class SignalResult {
    private final long ms;
    private final long length;
    private final boolean isSpectrum;

    public SignalResult(long ms, long length, boolean isSpectrum) {
        this.ms = ms;
        this.length = length;
        this.isSpectrum = isSpectrum;
    }

    public int getProgress(){
        int intExact = Math.toIntExact((ms * 50 / length * 2) / 2);
        return isSpectrum ?
                intExact + 50 : // Spectrum (phase 2)
                intExact; // Waveform (phase 1)
    }

    public String getPhase(){
        return isSpectrum ?
                "Wait for spectrum..." :
                "Wait for waveform...";
    }
}
