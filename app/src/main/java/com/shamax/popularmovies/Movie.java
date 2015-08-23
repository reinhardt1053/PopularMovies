package com.shamax.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable{
    private long id;
    private String posterPath;
    private String originalTitle;
    private String releaseDate;
    private String plotSynopsis;
    private float voteUserRating;

    public Movie(){};

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public float getVoteUserRating() {
        return voteUserRating;
    }

    public void setVoteUserRating(float voteUserRating) {
        this.voteUserRating = voteUserRating;
    }

    /* Parcelable related methods */
    @Override
    public int describeContents() { return 0; }

    private Movie(Parcel parcel) {
        id = parcel.readLong();
        posterPath = parcel.readString();
        originalTitle = parcel.readString();
        releaseDate = parcel.readString();
        plotSynopsis = parcel.readString();
        voteUserRating = parcel.readFloat();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(id);
        parcel.writeString(posterPath);
        parcel.writeString(originalTitle);
        parcel.writeString(releaseDate);
        parcel.writeString(plotSynopsis);
        parcel.writeFloat(voteUserRating);
    }

    public final static Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>(){
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }

    };
}
