package com.squishy.rickandmortyguide.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.squareup.picasso.Picasso;
import com.squishy.rickandmortyguide.R;
import com.squishy.rickandmortyguide.activities.EpisodeActivity;
import com.squishy.rickandmortyguide.models.Character;

public class CharacterDialog extends DialogFragment {

    public EpisodeActivity ctx;
    public Character character;

    private TextView characterLbl;
    private TextView statusLbl;
    private TextView originLbl;
    private TextView speciesLbl;
    private ImageView characterImg;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_character, container, false);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        characterLbl = v.findViewById(R.id.dialog_lbl_char);
        statusLbl = v.findViewById(R.id.dialog_lbl_status);
        originLbl = v.findViewById(R.id.dialog_lbl_origin);
        speciesLbl = v.findViewById(R.id.dialog_lbl_species);
        characterImg = v.findViewById(R.id.dialog_img_char);

        characterLbl.setText(character.name);
        statusLbl.setText(character.status);
        originLbl.setText(character.from);
        speciesLbl.setText(character.species);
        Picasso.with(ctx).load(character.image).into(characterImg);

        return v;
    }
}
