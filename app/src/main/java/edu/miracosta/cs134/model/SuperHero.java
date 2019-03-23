package edu.miracosta.cs134.model;

public class SuperHero
{
    private String mName;
    private String mSuperPower;
    private String mOneThing;
    private String mImageName;


    public SuperHero(String name, String superPower, String oneThing, String imageName)
    {
        mName = name;
        mSuperPower = superPower;
        mOneThing = oneThing;
        mImageName = imageName;
    }

    public String getName()
    {
        return mName;
    }

    public String getSuperPower()
    {
        return mSuperPower;
    }

    public String getOneThing()
    {
        return mOneThing;
    }

    public String getImageName()
    {
        return mImageName;
    }

    @Override
    public String toString()
    {
        return "SuperHero{" +
                "mName='" + mName + '\'' +
                ", mSuperPower='" + mSuperPower + '\'' +
                ", mOneThing='" + mOneThing + '\'' +
                ", mImageName='" + mImageName + '\'' +
                '}';
    }
}
