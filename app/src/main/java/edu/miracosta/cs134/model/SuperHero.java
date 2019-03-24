package edu.miracosta.cs134.model;

/**
 * Class to represent each superhero in CS134 spring 2019
 *
 * @author Dennis La
 * @version 1.0
 */
public class SuperHero
{
    private String mName;
    private String mSuperPower;
    private String mOneThing;
    private String mImageName;


    /**
     * Constructor for each hero.
     * @param name the name of the hero
     * @param superPower the superpower of the hero
     * @param oneThing the one thing you should know about the hero
     * @param imageName the file name of the image of the hero
     */
    public SuperHero(String name, String superPower, String oneThing, String imageName)
    {
        mName = name;
        mSuperPower = superPower;
        mOneThing = oneThing;
        mImageName = imageName;
    }

    /**
     * Getter for the name of the hero
     * @return the name of the hero
     */
    public String getName()
    {
        return mName;
    }

    /**
     * Getter for the super power of the hero
     * @return the super power of the hero
     */
    public String getSuperPower()
    {
        return mSuperPower;
    }

    /**
     * Getter for the one thing about the super hero
     * @return the one thing about the super hero
     */
    public String getOneThing()
    {
        return mOneThing;
    }

    /**
     * Getter for the image name of the super hero
     * @return the image name of the super hero
     */
    public String getImageName()
    {
        return mImageName;
    }

    /**
     * toString method of the hero
     * @return the string with all the information about the hero
     */
    @Override
    public String toString()
    {
        return "SuperHero{" +
                "Name='" + mName + '\'' +
                ", SuperPower='" + mSuperPower + '\'' +
                ", OneThing='" + mOneThing + '\'' +
                ", ImageName='" + mImageName + '\'' +
                '}';
    }
}
