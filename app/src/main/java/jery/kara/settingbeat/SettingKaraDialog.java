package jery.kara.settingbeat;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import jery.kara.R;
import jery.kara.manager.KaraManager;
import jery.kara.settingbeat.myview.IndicatorSeekBar;

/**
 * Created by CPU11341-local on 18-Apr-18.
 */

public class SettingKaraDialog extends DialogFragment {
    SeekBar beat_volume_seekbar;
    SeekBar mic_volume_seekbar;
    IndicatorSeekBar tone_seekbar;
    TextView beat_volume_seekbar_value;
    TextView mic_volume_seekbar_value;
    TextView tone_seekbar_value;
    TextView btn_stop;
    Switch switch_whatUHear;
    public int micVolume = 50;
    public int beatVolume = 50;
    public int tone = 0;
    public boolean isWhatUHearChecked = true;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_setting_kara, null);
        Dialog a = new Dialog(getActivity());
        a.requestWindowFeature(Window.FEATURE_NO_TITLE);
        a.setContentView(view);
        a.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        a.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        a.setCanceledOnTouchOutside(true);
        init(view);

        mic_volume_seekbar_value.setText(String.valueOf(micVolume));
        mic_volume_seekbar.setProgress(micVolume);

        beat_volume_seekbar_value.setText(String.valueOf(beatVolume));
        beat_volume_seekbar.setProgress(beatVolume);

        tone += 3;
        tone_seekbar_value.setText(String.valueOf(tone));
        tone_seekbar.setProgress(tone);

        switch_whatUHear.setChecked(isWhatUHearChecked);

        return a;
    }

    private void init(View view){
        beat_volume_seekbar = (SeekBar) view.findViewById(R.id.volume_seekbar);
        mic_volume_seekbar = (SeekBar) view.findViewById(R.id.mic_seekbar);
        tone_seekbar = (IndicatorSeekBar) view.findViewById(R.id.tone_seekbar);
        beat_volume_seekbar_value = (TextView) view.findViewById(R.id.volume_seekbar_value);
        mic_volume_seekbar_value = (TextView) view.findViewById(R.id.mic_seekbar_value);
        tone_seekbar_value = (TextView) view.findViewById(R.id.tone_seekbar_value);
        switch_whatUHear = (Switch) view.findViewById(R.id.switch_whatUHear);
        btn_stop = (TextView) view.findViewById(R.id.btn_stop);

        beat_volume_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                beat_volume_seekbar_value.setText(i + "%");
                onSettingChangeListener.onBeatVolumeSeekbarChange(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mic_volume_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mic_volume_seekbar_value.setText(i + "%");
                onSettingChangeListener.onMicVolumeSeekbarChange(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        tone_seekbar.setOnSlideListener(new IndicatorSeekBar.OnSlideListener() {
            @Override
            public void onSlide(int index) {
                int value = index - 3;
                tone_seekbar_value.setText(String.valueOf(value));
                onSettingChangeListener.onToneSeekbarChange(value);
            }
        });

        switch_whatUHear.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                onSettingChangeListener.onSwitchWhatUHearChange(b);
            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KaraManager.getInstance().onActionStopSong();
                dismiss();
            }
        });
    }

    public interface OnSettingChangeListener{
        void onBeatVolumeSeekbarChange(int value);
        void onMicVolumeSeekbarChange(int value);
        void onToneSeekbarChange(int value);
        void onSwitchWhatUHearChange(boolean b);
    }

    private OnSettingChangeListener onSettingChangeListener;

    public void setOnSettingChangeListener(OnSettingChangeListener onSettingChangeListener) {
        this.onSettingChangeListener = onSettingChangeListener;
    }
}
