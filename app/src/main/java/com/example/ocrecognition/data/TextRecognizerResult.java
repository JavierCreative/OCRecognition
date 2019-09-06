package com.example.ocrecognition.data;

import com.google.firebase.ml.vision.text.FirebaseVisionText;

import java.util.List;

public class TextRecognizerResult
{
    private List<FirebaseVisionText.TextBlock> bloques;

    public List<FirebaseVisionText.TextBlock> getBloques() {
        return bloques;
    }

    public void setBloques(List<FirebaseVisionText.TextBlock> bloques) {
        this.bloques = bloques;
    }
}
