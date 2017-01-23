package ramil.particulatematter.particlegen;


import net.minecraft.nbt.NBTTagCompound;

import java.lang.Math;

public class Particle {

    // particle attributes
    public String name;
    public int longevity;
    public int max_energy;
    public int energy_drain;
    public int optimal_energy;
    public int deviation;
    public int yield;

    // particle trackers
    public int ticks_left;
    public int current_energy;

    public EnumParticleDeath death_type = null;

    public Particle(String name, int longevity, int max_energy, int energy_drain, int optimal_energy, int yield, int deviation) {
        /*
        longevity: total possible lifecycle of particle (in ticks)
        max_energy: maximum Particulate Energy (PE) particle can withstand
        energy_drain: PE/t drained while in use
        optimal_energy: best possible PE to attain maximum RF/t yield; acts as "mean" for gaussian
        deviation: leniency of PE from optimal_energy threshold; acts as "deviation" for gaussian
        yield: RF/t at optimal_energy PE threshold
         */
        this.name = name;

        this.longevity = longevity;
        this.max_energy = max_energy;
        this.energy_drain = energy_drain;
        this.optimal_energy = optimal_energy;
        this.yield = yield;
        this.deviation = deviation;

        this.ticks_left = longevity;
        this.current_energy = optimal_energy;
    }

    public Particle(NBTTagCompound tagCompound) {
        this.name = tagCompound.getString("name");
        this.longevity = tagCompound.getInteger("longevity");
        this.max_energy = tagCompound.getInteger("max_energy");
        this.energy_drain = tagCompound.getInteger("energy_drain");
        this.optimal_energy = tagCompound.getInteger("optimal_energy");
        this.deviation = tagCompound.getInteger("deviation");
        this.yield = tagCompound.getInteger("yield");

        this.ticks_left = tagCompound.getInteger("ticks_left");
        this.current_energy = tagCompound.getInteger("current_energy");
    }

    public void updateParticle(NBTTagCompound tagCompound) {
        this.name = tagCompound.getString("name");
        this.longevity = tagCompound.getInteger("longevity");
        this.max_energy = tagCompound.getInteger("max_energy");
        this.energy_drain = tagCompound.getInteger("energy_drain");
        this.optimal_energy = tagCompound.getInteger("optimal_energy");
        this.deviation = tagCompound.getInteger("deviation");
        this.yield = tagCompound.getInteger("yield");

        this.ticks_left = tagCompound.getInteger("ticks_left");
        this.current_energy = tagCompound.getInteger("current_energy");
    }

    /*
    Checks if the particle is still active.
    ticks_left must be greater than 0
    current_energy must be between 0 and max_energy
     */
    public boolean isActive() {
        if (this.ticks_left > 0 && this.current_energy > 0 && this.current_energy <= this.max_energy) {
            // there are remaining ticks in the life cycle
            return true;
        } else {
            System.out.println("particle inactive at " + this.ticks_left + " ticks");
            // particle is dead, but how?
            if (this.ticks_left <= 0) {
                // it lived it's full lifecycle
                this.death_type = EnumParticleDeath.NATURAL;
                System.out.println("death-type natural");
            } else if (this.ticks_left > 0 && this.current_energy > this.max_energy) {
                // the particle was overcharged
                this.death_type = EnumParticleDeath.OVERCHARGE;
                System.out.println("death-type overcharge");
            } else if (this.ticks_left > 0 && this.current_energy <= 0) {
                // the particle was undercharged
                this.death_type = EnumParticleDeath.UNDERCHARGE;
                System.out.println("death-type undercharge");
            } else {
                // unknown particle death
                this.death_type = EnumParticleDeath.RANDOM;
                System.out.println("death-type random");
            }
            return false;
        }
    }

    /*
    Handles a single tick on this particle. Method should be called by
    Particle Chamber tile entity.
    energy_to_add: passed by the Particle Chamber tile entity if the
                   Particle Chamber block is hit with a laser.

                   Charge will be added BEFORE RF/t is calculated.
                   Energy drain will be applied AFTER RF/t is calculated.
     */
    public int tick(int energy_to_add) {
        if (!this.isActive()) {
            return 0;
        }

        this.ticks_left--;
        this.current_energy += energy_to_add;
        int rf = this.getTickRF();
        this.current_energy -= this.energy_drain;
        return rf;
    }

    private int getTickRF() {
        return (int) Math.ceil(this.gaussianDistribution(this.current_energy, this.optimal_energy, this.deviation) * this.yield);
    }

    private float gaussianDistribution(int x, int mu, int sig) {
        return (float) Math.exp(((float) -Math.pow(x - mu, 2.)) / ((float)(2 * Math.pow(sig, 2.))));
    }

    public NBTTagCompound getTagCompound() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setString("name", this.name);
        tagCompound.setInteger("longevity", this.longevity);
        tagCompound.setInteger("max_energy", this.max_energy);
        tagCompound.setInteger("energy_drain", this.energy_drain);
        tagCompound.setInteger("optimal_energy", this.optimal_energy);
        tagCompound.setInteger("deviation", this.deviation);
        tagCompound.setInteger("yield", this.yield);

        tagCompound.setInteger("ticks_left", this.ticks_left);
        tagCompound.setInteger("current_energy", this.current_energy);
        return tagCompound;
    }

}
