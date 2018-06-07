package it.polimi.se2018.view;

import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.WindowPattern;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.IOException;

public class WindowPatternPlayerView extends Pane {
    private Player player;

    private String nickname;
    private int favourTokens;
    private WindowPattern windowPattern;

    @FXML private Label nicknameLabel;
    @FXML private Label favourTokensLabel;
    @FXML private ImageView wpImage;
    @FXML private Label titleLabel;
    @FXML private ImageView userIcon;

    public WindowPatternPlayerView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/WindowPatternPlayerView.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
        this.nicknameLabel.setText(nickname);
    }

    public void setWindowPattern(WindowPattern wp) {
        this.windowPattern = wp;
        Image wpjpg = new Image((new File(windowPattern.getImageURL()).toURI().toString()));
        wpImage.setImage(wpjpg);
        titleLabel.setText(windowPattern.getTitle());
    }

    public void setFavourTokens(int ft) {
        this.favourTokens = ft;
        this.favourTokensLabel.setText(String.valueOf(ft));
    }


    public void setThisAsUser() {
        this.userIcon.setVisible(true);
    }

    public void setThisAsCurrentPlayer(boolean t) {
        if (t) {
            this.setBorder(new Border(new BorderStroke(Color.RED,
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5))));
        } else {
            this.setBorder(new Border(new BorderStroke(Color.YELLOWGREEN,
                    BorderStrokeStyle.NONE, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        }
    }

    public String getNickname() { return nickname; }
}
