Engine_Nameless_Nightmare : CroneEngine {

    var <bufferL;
	var <bufferR;
	var <recSynth;

	var mercury, venus, earth, mars, jupiter, saturn, uranus, neptune;
	var mercury_on=0, venus_on=0, earth_on=0, mars_on=0, jupiter_on=0, saturn_on=0, uranus_on=0, neptune_on=0;
	var gEnv1, gEnv2, gEnv3, gEnv4, gEnv5, gEnv6, gEnv7, mercuryEnvBuf, venusEnvBuf, earthEnvBuf, marsEnvBuf, jupiterEnvBuf, saturnEnvBuf, uranusEnvBuf;

	*new { arg context, doneCallback;
		^super.new(context, doneCallback);
	}

    readBuf { arg path;
		if (File.exists(path), {
			var numChannels;
			var buffer;

			var mercury_on=0, venus_on=0, earth_on=0, mars_on=0, jupiter_on=0, saturn_on=0, uranus_on=0, neptune_on=0;
			var turn_off_all = {mercury.set(\gate, 0); venus.set(\gate, 0); earth.set(\gate, 0); mars.set(\gate, 0); jupiter.set(\gate, 0); saturn.set(\gate, 0); uranus.set(\gate, 0); neptune.set(\gate, 0)};

			numChannels = SoundFile.use(path.asString(), { |f| f.numChannels });

			buffer = Buffer.readChannel(context.server, path, 0, -1, [0], { |b|
				bufferL.free;
				bufferL = b;
			});

			if (numChannels > 1, {
				buffer = Buffer.readChannel(context.server, path, 0, -1, [1], { |b|
					bufferR.free;
					bufferR = b;
				});
			}, {
				bufferR.free;
				bufferR = buffer;
			});
		});
	}

	recordToBuf {
		recSynth = { 
			RecordBuf.ar(
				SoundIn.ar(0),
				bufferL,
				doneAction: 2
			);
			RecordBuf.ar(
				SoundIn.ar(1),
				bufferR,
				doneAction: 2
			);
		}.play;
	}
  
	stopRecord { 
		if (recSynth.notNil, { recSynth.free; });
	}

    alloc {
        bufferL = Buffer.alloc(
				context.server,
				context.server.sampleRate * 1,
		);
		bufferR = Buffer.alloc(
				context.server,
				context.server.sampleRate * 1,
		);

			gEnv1 = Env([0, 1, 0.2, 0], [0.5, 0.5, 2.5], [-5, 2, -2]);
			gEnv2 = Env([0, 1, 0], [0.3, 0.1], [2, 8]);
			gEnv3 = Env([0, 1, 0.2, 0], [0.01, 0.3, 0.15], [2, 6, -8]);
			gEnv4 = Env([0, 0.2, 0, 1, 0], [0.01, 0.05, 0.15, 0.2], [5, -5, -3, 3]);
			gEnv5 = Env([0, 1, 0], [1, 1], [12, -8]);
			gEnv6 = Env([0, 0.7, 0.1, 1, 0], [0.01, 0.4, 0.4, 0.01], [-2, -12, 12 ,2]);
			gEnv7 = Env([0, 1, 0], [1, 2], [-2, -5]);
			mercuryEnvBuf = Buffer.sendCollection(context.server, gEnv1.discretize, 1);
			venusEnvBuf = Buffer.sendCollection(context.server, gEnv2.discretize, 1);
			earthEnvBuf = Buffer.sendCollection(context.server, gEnv3.discretize, 1);
			marsEnvBuf = Buffer.sendCollection(context.server, gEnv4.discretize, 1);
			jupiterEnvBuf = Buffer.sendCollection(context.server, gEnv5.discretize, 1);
			saturnEnvBuf = Buffer.sendCollection(context.server, gEnv6.discretize, 1);
			uranusEnvBuf = Buffer.sendCollection(context.server, gEnv7.discretize, 1);

        //Mercury
		SynthDef(\mercury, {
			arg buf_l, buf_r, gate=0, mercury=0, vulcan=0, px=0, sun=0;
			var sig, env;

			//mercury
			sig = GrainBuf.ar(
				2,
				Impulse.ar(SinOsc.ar(0.005, [0.333, 0.666], 0.2*(1+(sun/100)), 44*(1+(sun/100)))),
				[(0.12 + LFNoise1.kr(1.2, 0.06))*(mercury/100), 0.1*(mercury/100)],
				[buf_l, buf_r],
				[0.24 + SinOsc.ar(0.04 - (vulcan/300), 0, 0.01), 0.24 + SinOsc.ar(0.04, 0, 0.08)],
				[0.2 + LFSaw.ar(0.04, 0, 0.15), 0.8 - Dust.ar(1.5+(vulcan/300), 0.15)],
				3,
				[-1*vulcan/100, vulcan/100],
				mercuryEnvBuf,
				16,
			);

			sig = LeakDC.ar(sig);
			sig = Splay.ar(sig);
			sig = LPF.ar(sig, 800 + vulcan * 4);
			sig = Splay.ar(sig, vulcan/100);

			//sig = sig * 3.dbamp;
			env = EnvGen.kr(Env.asr(2, 1, 4), gate: gate, doneAction: 2);
			sig = sig * env * (mercury/100);

			//effect
			sig = sig + DelayC.ar(PitchShift.ar(sig, [0.2, 0.21], 0.75 + (0.25*(px/100)), mul: 0.3*(1+(px/100))), 0.15, [0.1, 0.12]);

			sig = Limiter.ar(sig);

			Out.ar(0, sig);
		}).add;


		//Venus
		SynthDef(\venus, {
			arg buf_l, buf_r, gate=0, venus=0, zoozve=0, px=0, sun=0;
			var sig, env;
			sig = GrainBuf.ar(
				3,
				Impulse.ar(LFNoise1.kr(0.0679, 12* (1+(sun/100)), 48 * (1+(sun/100)))),
				LFSaw.kr([0.16,0.07,0.15] + LFNoise1.kr(0.06, 0.24), [-1, -0.4, 0.8], 0.05*(venus/100), 0.051*(venus/100)),
				[buf_l, buf_r, buf_l],
				[-1.5, -0.5, -2.25],
				SinOsc.ar(0.00235, [0.34, 0.254, 0.635], 0.05, [0.33333, 0.66666, 0.75999]),
				1,
				[LFNoise1.kr(0.2, 0.05, -0.5),LFNoise1.kr(0.2, 0.25),LFNoise1.kr(0.2, 0.45, 0.5)],
				venusEnvBuf,
				16,
				[0.1 + 0.3*(zoozve/100), 1, 0.1 + 0.3*(zoozve/100)]
			);
			sig = LeakDC.ar(sig);
			sig = Splay.ar(sig, 0.7+(0.3*(zoozve/100)));

			sig = sig + LPF.ar(DelayC.ar(sig, 0.25, Saw.ar([0.3*(zoozve/100),0.4*(zoozve/100)], [1+Dust.ar(0.03), 2pi-Dust.ar(0.03)], 0.06*(zoozve/100), 0.08)), 500+(zoozve*4), zoozve/100);

			sig = sig.tanh;
			env = EnvGen.kr(Env.asr(2, 1, 4), gate: gate, doneAction: 2);
			sig = sig * env * (venus/100);

			//effect
			sig = sig + PitchShift.ar(sig, 0.2, SinOsc.kr(0.3, [-2, 3pi], 0.02, 2), 0, 0.22, 0.3*(1+(px/100)));


			sig = Limiter.ar(sig);

			Out.ar(0, sig);
		}).add;


		//Earth
		SynthDef(\earth, {
			arg buf_l, buf_r, gate=0, earth=0, moon=0, satellites=0, px=0, sun=0;
			var sig, sig1, env, env1, moon_amp, moon_sel, earthshake, moon_theia, theiashake, earth_pan;

			moon_sel = ((moon-50)/50).atan2(0.1).round(1) + 1;
			moon_amp = Select.kr(moon_sel, [moon/100,1,moon/120]);
			earthshake = Select.kr(moon_sel, [0-(moon/24),LFNoise1.kr(16,20),SinOsc.ar(8, 0, 4+(moon/200))]);
			moon_theia = Select.kr(moon_sel, [0,0,-1]);
			theiashake = Select.kr(moon_sel, [0,0,(moon/24)-12]);
			earth_pan = Select.kr(moon_sel, [0,0,(moon-50)/50]);

			//earth
			sig = GrainBuf.ar(
				2,
				Impulse.ar(50 * (1 + (sun/100))),
				SinOsc.ar(0.00815 + (satellites*0.001), 0.499, 0.0023 * (earth/100), (0.0086 + (satellites*0.0005)) * (earth/100)),
				[buf_l, buf_r],
				SinOsc.ar(0.00486, 0.254, 0.0125, 0.934),
				[SinOsc.ar(0.0032 + LFNoise1.kr(0.02, 0.001), 0.2, 0.4, 0.5), SinOsc.ar(0.0034 + LFNoise1.kr(0.02, 0.001), 0.6pi, 0.3, 0.6)],
				3,
				[0, earth_pan],
				earthEnvBuf,
				16
			);
			sig = sig * -3.dbamp;
			sig = LeakDC.ar(sig);
			sig = sig.tanh;
			sig = Splay.ar(sig, 1);

			//moon / theia
			sig1 = GrainBuf.ar(
				2,
				Impulse.kr((31 + (20*((moon-50)/50).atan2(0.1))) * (1+(sun/100))),
				SinOsc.ar(0.00815 + (satellites*0.001), 0.499, 0.0083*(moon/100), (0.0236 + (satellites*0.0003))*(moon/100)),
				[buf_r, buf_l],
				SinOsc.ar(0.00486, 0.254, 0.0125, 1.934),
				[SinOsc.ar((0.0032 + LFNoise1.kr(0.02, 0.001)), -1, 0.4, 0.5), SinOsc.ar((0.0034 + LFNoise1.kr(0.02, 0.001)), -pi, 0.3, 0.6)],
				3,
				[0-earth_pan, 0],
				earthEnvBuf,
				16
			);
			sig1 = sig1 * -3.dbamp;
			sig1 = sig1.tanh;
			sig1 = Splay.ar(sig1, 1);

			env = EnvGen.kr(Env.asr(2, 1, 4), gate: gate, doneAction: 2);
			env1 = EnvGen.kr(Env.asr(2, 1, 4), gate: gate, doneAction: 2);
			sig = (sig * env * (earth/100)) + (sig1 * env1 * (moon/100));

			//effect
			sig = sig + CombC.ar(sig, 0.4, [LFNoise2.ar(0.1, 0.18, 0.2), LFNoise2.ar(0.1, 0.18, 0.2)], [SinOsc.ar(0.2, pi, 0.4, 0.8), SinOsc.ar(0.2, -pi, 0.4, 0.8)], 0.4*(1+(px/100)));

			sig = Limiter.ar(sig);

			Out.ar(0, sig);
		}).add;


		//Mars
		SynthDef(\mars, {
			arg buf_l, buf_r, gate=0, mars=0, phobos=0, deimos=0, px=0, sun=0;
			var sig, sig1, sig2, env, env1, env2, phobos_amp, phobos_sel, deimos_amp, deimos_sel, marsshake, phobosshake, rand;

			phobos_sel = ((phobos-50)/50).atan2(0.1).round(1) + 1;
			phobos_amp = Select.kr(phobos_sel, [phobos/100,0.5,0.25+(phobos/200)]);

			deimos_sel = ((deimos-50)/50).atan2(0.5).round(1) + 1;
			deimos_amp = Select.kr(deimos_sel, [deimos/100,0.5,0.25+(deimos/200)]);

			marsshake = Select.kr(phobos, [0,20,13]);
			phobosshake = Select.ar(phobos_sel, [Impulse.ar(12 * (1 + (sun/100))),Impulse.ar(12 * (1 + (sun/100))),Dust.ar(36 * (1 + (sun/100)))]);

			rand = Rand(-pi, pi);

			//mars
			sig = GrainBuf.ar(
				2,
				Impulse.ar((48 + [LFNoise1.ar(0.02, 1.4), LFNoise1.ar(0.02, 1.4)]) * (1+(sun/100))),
				SinOsc.ar(0.029, 0.6, 0.01*(mars/100), 0.025*(mars/100)),
				[buf_l, buf_r],
				LFSaw.ar(0.00747, 0, 0.66, 0.7) + LFPulse.ar(LFNoise0.ar(2, 0.08, 0.2), 0.5, 0.01, (mars/100), 1),
				LFSaw.ar(0.00025, rand, 0.49, 0.5),
				3,
				0,
				marsEnvBuf,
				16
			);
			sig = LeakDC.ar(sig);
			sig = Splay.ar(sig);
			sig = sig * -3.dbamp;
			sig = sig.tanh;

			//phobos
			sig1 = GrainBuf.ar(
				1,
				phobosshake,
				0.08 + (-0.02*((phobos-50)/50).atan2(0.1)),
				buf_l,
				0.8 + SinOsc.ar(0.024, pi, 0.08),
				LFSaw.ar(0.00025, rand, 0.49, 0.5),
				3,
				0,
				marsEnvBuf,
				16
			);
			sig1 = LeakDC.ar(sig1);
			sig1 = Splay.ar(sig1);
			sig1 = HPF.ar(sig1, 1000);
			sig1 = sig1 * -3.dbamp;
			sig1 = sig1.tanh;

			//deimos
			sig2 = GrainBuf.ar(
				1,
				Impulse.ar(12 * (1 + (sun/100))),
				0.1 + (0.1*(deimos/100)),
				buf_r,
				0.8 + SinOsc.ar(0.024, 0, 0.08),
				LFSaw.ar(0.00025, rand, 0.49, 0.5),
				3,
				0,
				marsEnvBuf,
				16
			);
			sig2 = LeakDC.ar(sig2);
			sig2 = Splay.ar(sig2);
			sig2 = LPF.ar(sig2, 1000);
			sig2 = sig2 * -3.dbamp;
			sig2 = sig2.tanh;

			env = EnvGen.kr(Env.asr(2, 1, 4), gate: gate, doneAction: 2);
			env1 = EnvGen.kr(Env.asr(2, 1, 4), gate: gate, doneAction: 2);
			env2 = EnvGen.kr(Env.asr(2, 1, 4), gate: gate, doneAction: 2);
			sig = (sig * env * (mars/100)) + (sig1 * env1 * phobos_amp) + (sig2 * env2 * deimos_amp);

			//effect
			sig = sig + LPF.ar(DelayC.ar(sig, 0.61, LFNoise0.ar(LFNoise1.kr(0.2, 0.8, 0.81), 0.3, 0.301)), 1000, 0.3*(1+(px/100))) + HPF.ar(DelayC.ar(sig, 1.21, LFPulse.ar(LFNoise0.ar([2, 2.1], 0.08, 0.2), 0.5, 0.01, 0.6*(mars/100), 0.601)), 1000, 0.5*(1+(px/100)));

			sig = Limiter.ar(sig);
			Out.ar(0, sig);
		}).add;


		//Jupiter
		SynthDef(\jupiter, {
			arg buf_l, buf_r, gate=0, jupiter=0, moons=0, ring=0, px=0, sun=0;
			var sig, sig1, env, env1, moons_mod, moons_amp;

			moons_amp = Select.kr((moons/100).round(1), [moons/50, 1]);
			moons_mod = Select.kr((moons/100).round(1), [0, 0.0022*(moons-50)]);

			sig = GrainBuf.ar(
				2,
				Impulse.ar(LFSaw.ar(0.06, mul: 4*(1+(sun/100)), add: 8*(1+(sun/100)))),
				SinOsc.ar(0.008333, 0, (0.04 + (ring * 0.00005))*(jupiter/100), (0.15 + (ring * 0.0012))*(jupiter/100)),
				[buf_l, buf_r],
				0.7 + LFNoise1.ar(0.1, 0.5),
				Impulse.ar(LFSaw.ar(0.0012, mul: 5, add: 5.1) + LFNoise1.ar(0.08333, 0.2), mul: LFSaw.ar(-0.01, mul: 5, add: 5.1), add: LFNoise0.ar(0.12, 0.1, 0.4)),
				3,
				LFNoise1.ar(LFSaw.ar(0.06, mul: 4, add: 8), 0.4),
				jupiterEnvBuf,
				4
			);
			sig = LeakDC.ar(sig);
			sig = Splay.ar(sig);
			sig = RLPF.ar(sig, LFSaw.ar(0.01, mul: 600-(ring*2), add: 1200+(ring*8)), 2);
			sig = sig * 6.dbamp;
			sig = sig.tanh;
			sig = sig * -3.dbamp;

			//moons
			sig1 = GrainBuf.ar(
				2,
				Impulse.ar(LFSaw.ar(0.06, 0, 1+(sun/100), [1.001, 2.008, 4.019, 9.4]*(1+(sun/100)))),
				SinOsc.ar(0.008333, 0, 0.05, 0.1) + moons_mod,
				[buf_l, buf_r, buf_l, buf_r],
				1.2,
				[0.2, 0.4, 0.6, 0.8],
				1,
				SinOsc.kr(0.024 + moons_mod, [0, pi/2, pi, 3pi/2]),
				jupiterEnvBuf,
				4
			);
			sig1 = LeakDC.ar(sig1);
			sig1 = Splay.ar(sig1, 0.9, 0.8);
			sig1 = sig1 * -5.dbamp;

			env = EnvGen.kr(Env.asr(2, 1, 4), gate: gate, doneAction: 2);
			env1 = EnvGen.kr(Env.asr(2, 1, 4), gate: gate, doneAction: 2);

			sig = (sig * env * (jupiter/100)) + (sig1 * env1 * (moons/100));
			sig = sig * -3.dbamp;

			//effect
			sig = sig + FreqShift.ar(CombC.ar(sig * -3.dbamp, 0.8, XLine.kr(0.8, 0.2, 0.4), 1.2*(1+(px/101)), 0.4*(1+(px/100))));

			sig = Limiter.ar(sig);
			Out.ar(0, sig);
		}).add;


		//Saturn
		SynthDef(\saturn, {
			arg buf_l, buf_r, gate=0, saturn=0, ring=0, mimas=0, rhea=0, titan=0, px=0, sun=0;
			var sig, sig1, sig2, sig3, sig4, env, env1, env2, env4;

			//saturn
			sig = GrainBuf.ar(
				2,
				Dust.ar((1.2 + LFNoise0.ar(1, 1))*(1+(sun/100))),
				SinOsc.ar(0.1, 0, [0.05, 0.06]*(0.1+(saturn/100)), LFSaw.ar(0.02, 0, 0.06*(0.1+(saturn/100)), 0.1*(0.1+(saturn/100)))),
				[buf_l, buf_r],
				2.6,
				Phasor.ar(Impulse.ar(12), 4000, [0.22, 0.2], LFSaw.ar(0.06, 0, 0.07, 0.3), [0.22, 0.2]),
				1,
				LFNoise0.ar(1, 0.8),
				saturnEnvBuf,
				8
			);
			sig = LeakDC.ar(sig);
			sig = Splay.ar(sig);
			sig = sig * 3.dbamp;
			sig = sig.tanh;

			//rhea
			sig3 = DelayN.ar(sig, 0.68, 0.68, rhea/100);
			sig3 = PitchShift.ar(sig3, 0.3, 2);
			sig3 = Pan2.ar(sig3, LFNoise1.kr(0.24));

			//ring
			sig1 = GrainBuf.ar(
				2,
				Impulse.ar(SinOsc.kr(0.024, [0, pi/3, 2pi/3, pi], 10*(1+(sun/100)), 10*(1+(sun/100)))),
				0.018 - (mimas*0.00006),
				[buf_l, buf_r, buf_l, buf_r],
				SinOsc.kr(0.024, [0, pi/3, 2pi/3, pi], 0.5, 0),
				[0.15, 0.3, 0.45, 0.6],
				1,
				SinOsc.kr(0.024, [0, pi/3, 2pi/3, pi]),
				saturnEnvBuf,
				4
			);
			sig1 = LeakDC.ar(sig1);
			sig1 = Splay.ar(sig1, 0.9, 1.0);

			//mimas
			sig2 = sig2 = BPF.ar(DelayC.ar(sig, 0.5, SinOsc.ar(2, [pi, 0], 0.25, 0.25), mimas/100), SinOsc.ar(2, [0, pi], 400, 600), 0.2*(1+(mimas/100)));
			sig2 = Splay.ar(sig2, 0.9, 1.0);

			//titan
			sig4 = GrainBuf.ar(
				2,
				Impulse.ar(44*(1+(sun/100))),
				SinOsc.ar(0.00815, 0.499, 0.0083, 0.0166),
				[buf_l, buf_r],
				0.4,
				Phasor.ar(Impulse.ar(12), 4000, [0.22, 0.2], LFSaw.ar(0.06, 0, 0.07, 0.3), [0.22, 0.2]),
				1,
				0,
				earthEnvBuf,
				4
			);
			sig4 = LeakDC.ar(sig4);
			sig4 = Splay.ar(sig4, 0.3);
			sig4 = sig4 * -6.dbamp;

			env = EnvGen.kr(Env.asr(2, 1, 4), gate: gate, doneAction: 2);
			env1 = EnvGen.kr(Env.asr(2, 1, 4), gate: gate, doneAction: 2);
			env2 = EnvGen.kr(Env.asr(2, 1, 4), gate: gate, doneAction: 2);
			env4 = EnvGen.kr(Env.asr(2, 1, 4), gate: gate, doneAction: 2);

			sig = (sig * env * (saturn/100)) + (sig1 * env1 * (ring/100)) + (sig2 * env2 * (mimas/100)) + sig3 + (sig4 * env4 * (titan/100));

			sig = FreeVerb2.ar(sig[0], sig[1], 0.8, 0.9+(px/1000), 0.6+(px/300), 0.6+(px/300));
			sig = Limiter.ar(sig);
			Out.ar(0, sig);
		}).add;

		//Uranus
		SynthDef(\uranus, {
			arg buf_l, buf_r, gate=0, uranus=0, titania=0, oberon=0, ring=0, px=0, sun=0;
			var sig, sig1, sig2, sig3, env, env1, env2, env3, noise1, noise2;

			noise1 = LFNoise1.kr(0.126, 0.5 * (titania/100), 1);
			noise2 = LFNoise1.kr(0.126, 0.5 * (oberon /100), 1);

			//uranus
			sig = GrainBuf.ar(
				2,
				[Dust.ar(12*(1+(sun/100))), Dust.ar(12*(1+(sun/100)))],
				LFSaw.kr(0.1, [pi, 2], 0.2*(uranus/101), 0.3*(uranus/101)),
				[buf_l, buf_r],
				SinOsc.ar([0.006, 0.007], mul: 0.06, add: -1),
				Impulse.ar([noise1, noise2], mul: 0.3, add: SinOsc.ar([noise1, noise2], mul: 0.3, add: 1/3)),
				3,
				[-1 * noise1, noise2],
				uranusEnvBuf,
				16
			);
			sig = LeakDC.ar(sig);
			sig = Splay.ar(sig, 0.6);
			sig = sig.tanh;
			sig = LPF.ar(sig, 7000);

			//titania
			sig1 = GrainBuf.ar(
				1,
				Dust.ar(40*(1+(sun/100))),
				SinOsc.ar(0.1, 0, 0.05*(titania/101), 0.6*(titania/101)),
				buf_l,
				1,
				Impulse.ar(Impulse.ar(10,0,5,5), mul: 0.03, add: SinOsc.ar(0.25, mul: 0.02, add: 0.2)),
				1,
				0,
				uranusEnvBuf,
				32
			);
			sig1 = LeakDC.ar(sig1);
			sig1 = Splay.ar(sig1);
			sig1 = sig1 * -8.dbamp;

			//oberon
			sig2 = GrainBuf.ar(
				1,
				Dust.ar(40*(1+(sun/100))),
				SinOsc.ar(0.1, 0, 0.05*(oberon/101), 0.6*(oberon/101)),
				buf_r,
				1,
				Impulse.ar(Impulse.ar(10,0,5,5), mul: 0.03, add: SinOsc.ar(0.25, mul: 0.02, add: 0.4)),
				1,
				0,
				uranusEnvBuf,
				32
			);
			sig2 = LeakDC.ar(sig2);
			sig2 = Splay.ar(sig2);
			sig2 = sig2 * -8.dbamp;

			//ring
			sig3 = GrainBuf.ar(
				2,
				Impulse.ar(((1000*(1+[noise1, noise2])) ++ SinOsc.kr(0.329, [0,pi], 160))*(1+(sun/100))),
				LFNoise0.kr(1, 0.0007, 0.001),
				[buf_l, buf_r],
				2 + SinOsc.kr(0.129, [0,pi]),
				[0.4, 0.6] ++ SinOsc.ar(0.25, mul: 0.02, add: 0.3),
				3,
				SinOsc.kr(0.229, [0,pi]),
				uranusEnvBuf,
				16
			);
			sig3 = sig3 * -5.dbamp;

			env = EnvGen.kr(Env.asr(2, 1, 4), gate: gate, doneAction: 2);
			env1 = EnvGen.kr(Env.asr(2, 1, 4), gate: gate, doneAction: 2);
			env2 = EnvGen.kr(Env.asr(2, 1, 4), gate: gate, doneAction: 2);
			env3 = EnvGen.kr(Env.asr(2, 1, 4), gate: gate, doneAction: 2);

			sig = (sig * env * (uranus/100)) + (Pan2.ar(sig1, -0.8) * env1 * (titania/100)) + (Pan2.ar(sig2, 0.8) * env2 * (oberon/100)) + (sig3 * env3 * (ring/100));

			sig = sig + DelayN.ar(FreeVerb2.ar(sig[0], sig[1], 0.88, 0.6+(px/500), 0.6+(px/300), 0.7));
			sig = Limiter.ar(sig);
			Out.ar(0, sig);
		}).add;

		//Neptune
		SynthDef(\neptune, {
			arg buf_l, buf_r, gate=0, neptune=0, triton=0, nereid=0, pluto=0, orcus=0, ring=0, px=0, sun=0;
			var sig, sig1, sig2, sig3, sig4, sig5, env, env1, env2, env3, env4, env5;

			//neptune
			sig = GrainBuf.ar(
				2,
				Impulse.ar(LFTri.kr(0.044, -0.3, 3.888*(1+(sun/100)), 4*(1+(sun/100)))),
				LFSaw.ar(LFPulse.kr(Dust.ar(0.07), 0, LFNoise0.ar(0.002, 0.2, 0.4), 0.8) + 1.2, -1, 0.038*(neptune/101), 0.102*(neptune/101)),
				[buf_l, buf_r],
				LFNoise0.ar(0.3, 0.428, 0.8),
				[LFSaw.ar(LFNoise0.ar(0.1, 100, 104), 0, -0.1, 0.3), LFSaw.ar(LFNoise0.ar(0.1, 100, 104), 0, -0.15, 0.75)],
				3,
				0,
				[earthEnvBuf, venusEnvBuf],
				4
			);
			sig = LeakDC.ar(sig);
			sig = Splay.ar(sig, LFNoise2.ar(0.2, 0.6));
			sig = sig * -3.dbamp;

			//triton
			sig1 = GrainBuf.ar(
				1,
				Impulse.ar(LFTri.kr(0.044, -0.3, 3.888*(1+(sun/100)), 4*(1+(sun/100)))),
				SinOsc.kr(0.044, pi/2, 0.018*(triton/101), 0.02*(triton/101)),
				buf_r,
				-1.2,
				LFSaw.ar(LFNoise0.ar(0.1, 100, 104), 0, -0.15, 0.75),
				1,
				0,
				jupiterEnvBuf,
				8
			);
			sig1 = LeakDC.ar(sig1);
			sig1 = HPF.ar(sig1, 80);
			sig1 = PitchShift.ar(sig1, 0.3, 1.1);

			//nereid
			sig2 = GrainBuf.ar(
				1,
				Impulse.ar(LFTri.kr(0.044, -0.3, 3.888*(1+(sun/100)), 4*(1+(sun/100)))),
				SinOsc.kr(0.044, -pi/2, 0.018*(nereid/101), 0.02*(nereid/101)),
				[buf_l],
				1.2,
				LFSaw.ar(LFNoise0.ar(0.1, 100, 104), 0, -0.1, 0.3),
				1,
				0,
				jupiterEnvBuf,
				8
			);
			sig2 = Splay.ar(sig2);
			sig2 = HPF.ar(sig2, 80);
			sig2 = PitchShift.ar(sig2, 0.3, 1.1);

			//pluto
			sig3 = GrainBuf.ar(
				2,
				Impulse.ar(LFTri.kr(0.044, -0.3, 7.777*(1+(sun/100)), 8*(1+(sun/100))).clip(0, 20)),
				LFTri.ar(0.057, -pi/2, 0.17*(pluto/101), 0.08*(pluto/101)).clip(0.01, 0.2),
				buf_l,
				LFTri.ar(0.057, -pi/2, 0.5, 0.9).clip(0.8, 1.3),
				LFSaw.kr(0.005, pi/2, 0.5, 0.5),
				3,
				LFTri.kr(0.0057, pi/2, 0.25, -0.75),
				saturnEnvBuf,
				8
			);
			sig3 = LeakDC.ar(sig3);
			sig3 = Splay.ar(sig3);
			sig3 = sig3 * -6.dbamp;

			//orcus
			sig4 = GrainBuf.ar(
				2,
				Impulse.ar(LFTri.ar(0.057, pi/2, 7.777*(1+(sun/100)), 8*(1+(sun/100))).clip(0, 20)),
				LFTri.ar(0.057, pi/2, 0.17*(orcus/101), 0.08*(orcus/101)).clip(0.01, 0.2),
				buf_r,
				LFTri.ar(0.057, pi/2, 0.5, 0.9).clip(0.8, 1.3),
				LFSaw.kr(0.005, -pi/2, 0.5, 0.5),
				3,
				LFTri.kr(0.0057, -pi/2, 0.25, 0.75),
				uranusEnvBuf,
				8
			);
			sig4 = LeakDC.ar(sig4);
			sig4 = Splay.ar(sig4);
			sig4 = sig4 * -6.dbamp;

			sig5 = DelayC.ar(SinOsc.ar(LFTri.kr(0.0057, [pi/2, -pi/2], 200, 300), 0, sig3 + sig4), 1, LFTri.ar(0.057, 0, 0.8, 0.5).clip(0.1, 1));
			//sig5 = sig5 * -5.dbamp;
			sig5 = HPF.ar(sig5, 1200);

			env = EnvGen.kr(Env.asr(2, 1, 4), gate: gate, doneAction: 2);
			env1 = EnvGen.kr(Env.asr(2, 1, 4), gate: gate, doneAction: 2);
			env2 = EnvGen.kr(Env.asr(2, 1, 4), gate: gate, doneAction: 2);
			env3 = EnvGen.kr(Env.asr(2, 1, 4), gate: gate, doneAction: 2);
			env4 = EnvGen.kr(Env.asr(2, 1, 4), gate: gate, doneAction: 2);
			env5 = EnvGen.kr(Env.asr(2, 1, 4), gate: gate, doneAction: 2);

			sig = (sig * env * (neptune/100)) + (Pan2.ar(sig1, 0.7) * env1 * (triton/100)) + (Pan2.ar(sig2, -0.7) * env2 * (nereid/100)) + (sig3 * env3 * (pluto/100)) + (sig4 * env4 * (orcus/100)) + (sig5 * env5 * (ring/100));

			sig = FreeVerb2.ar(DelayC.ar(sig[0], 0.2, LFNoise2.ar(3,0.005, 0.01)), DelayC.ar(sig[1], 0.2, LFNoise2.ar(3,0.005, 0.01)), 0.4, 0.6*(1+(px/120)), 0.6+(px/300));
			sig = Limiter.ar(sig);
			Out.ar(0, sig);
		}).add;

		context.server.sync;


		this.addCommand("recStart", "i", { arg msg;
			"record start".postln;
			bufferL.zero;
			bufferR.zero;
			this.recordToBuf()
		});

		this.addCommand("recEnd", "i", { arg msg;
			"record end".postln;
			this.stopRecord()
		});
		
		this.addCommand("read", "s", { arg msg;
			this.readBuf(msg[1])
		});

		//mercury
		this.addCommand("mercuryGate", "ffff", { arg msg;
			if (mercury_on == 0,
				{mercury = Synth.new(\mercury, [
					\buf_l, bufferL,
					\buf_r, bufferR,
					\mercury, msg[1],
					\vulcan, msg[2],
					\px, msg[3],
					\sun, msg[4],
					\gate, 1
				]); mercury_on = 1},
				{mercury.set(\gate, 0); mercury_on = 0}
			);
		});

		this.addCommand("mercuryPrimary", "f", { arg msg;
			mercury.set(\mercury, msg[1])
		});
		this.addCommand("mercuryVulcan", "f", { arg msg;
			mercury.set(\vulcan, msg[1])
		});


		//venus
		this.addCommand("venusGate", "ffff", { arg msg;
			if (venus_on == 0,
				{venus = Synth.new(\venus, [
					\buf_l, bufferL,
					\buf_r, bufferR,
					\venus, msg[1],
					\zoozve, msg[2],
					\px, msg[3],
					\sun, msg[4],
					\gate, 1
				]); venus_on = 1},
				{venus.set(\gate, 0); venus_on = 0}
			);
		});

		this.addCommand("venusPrimary", "f", { arg msg;
			venus.set(\venus, msg[1])
		});

		this.addCommand("venusZoozve", "f", { arg msg;
			venus.set(\zoozve, msg[1])
		});


		//earth
		this.addCommand("earthGate", "fffff", { arg msg;
			if (earth_on == 0,
				{earth = Synth.new(\earth, [
					\buf_l, bufferL,
					\buf_r, bufferR,
					\earth, msg[1],
					\moon, msg[2],
					\satellites, msg[3],
					\px, msg[4],
					\sun, msg[5],
					\gate, 1
				]); earth_on = 1},
				{earth.set(\gate, 0); earth_on = 0}
			);
		});

		this.addCommand("earthPrimary", "f", { arg msg;
			earth.set(\earth, msg[1])
		});

		this.addCommand("earthMoon", "f", { arg msg;
			earth.set(\moon, msg[1])
		});

		this.addCommand("earthSatellites", "f", { arg msg;
			earth.set(\satellites, msg[1])
		});


		//mars
		this.addCommand("marsGate", "fffff", { arg msg;
			if (mars_on == 0,
				{mars = Synth.new(\mars, [
					\buf_l, bufferL,
					\buf_r, bufferR,
					\mars, msg[1],
					\phobos, msg[2],
					\deimos, msg[3],
					\px, msg[4],
					\sun, msg[5],
					\gate, 1
				]); mars_on = 1},
				{mars.set(\gate, 0); mars_on = 0}
			);
		});

		this.addCommand("marsPrimary", "f", { arg msg;
			mars.set(\mars, msg[1])
		});

		this.addCommand("marsPhobos", "f", { arg msg;
			mars.set(\phobos, msg[1])
		});

		this.addCommand("marsDeimos", "f", { arg msg;
			mars.set(\deimos, msg[1])
		});


		//jupiter
		this.addCommand("jupiterGate", "fffff", { arg msg;
			if (jupiter_on == 0,
				{jupiter = Synth.new(\jupiter, [
					\buf_l, bufferL,
					\buf_r, bufferR,
					\jupiter, msg[1],
					\moons, msg[2],
					\ring, msg[3],
					\px, msg[4],
					\sun, msg[5],
					\gate, 1
				]); jupiter_on = 1},
				{jupiter.set(\gate, 0); jupiter_on = 0}
			);
		});

		this.addCommand("jupiterPrimary", "f", { arg msg;
			jupiter.set(\jupiter, msg[1])
		});

		this.addCommand("jupiterMoons", "f", { arg msg;
			jupiter.set(\moons, msg[1])
		});

		this.addCommand("jupiterRing", "f", { arg msg;
			jupiter.set(\ring, msg[1])
		});


		//saturn
		this.addCommand("saturnGate", "fffffff", { arg msg;
			if (saturn_on == 0,
				{saturn = Synth.new(\saturn, [
					\buf_l, bufferL,
					\buf_r, bufferR,
					\saturn, msg[1],
					\mimas, msg[2],
					\rhea, msg[3],
					\titan, msg[4],
					\ring, msg[5],
					\px, msg[6],
					\sun, msg[7],
					\gate, 1
				]); saturn_on = 1},
				{saturn.set(\gate, 0); saturn_on = 0}
			);
		});

		this.addCommand("saturnPrimary", "f", { arg msg;
			saturn.set(\saturn, msg[1])
		});

		this.addCommand("saturnMimas", "f", { arg msg;
			saturn.set(\mimas, msg[1])
		});

		this.addCommand("saturnRhea", "f", { arg msg;
			saturn.set(\rhea, msg[1])
		});

		this.addCommand("saturnTitan", "f", { arg msg;
			saturn.set(\titan, msg[1])
		});

		this.addCommand("saturnRing", "f", { arg msg;
			saturn.set(\ring, msg[1])
		});


		//uranus
		this.addCommand("uranusGate", "ffffff", { arg msg;
			if (uranus_on == 0,
				{uranus = Synth.new(\uranus, [
					\buf_l, bufferL,
					\buf_r, bufferR,
					\uranus, msg[1],
					\titania, msg[2],
					\oberon, msg[3],
					\ring, msg[4],
					\px, msg[5],
					\sun, msg[6],
					\gate, 1
				]); uranus_on = 1},
				{uranus.set(\gate, 0); uranus_on = 0}
			);
		});

		this.addCommand("uranusPrimary", "f", { arg msg;
			uranus.set(\uranus, msg[1])
		});

		this.addCommand("uranusTitania", "f", { arg msg;
			uranus.set(\titania, msg[1])
		});

		this.addCommand("uranusOberon", "f", { arg msg;
			uranus.set(\oberon, msg[1])
		});

		this.addCommand("uranusRing", "f", { arg msg;
			uranus.set(\ring, msg[1])
		});


		//neptune
		this.addCommand("neptuneGate", "ffffffff", { arg msg;
			if (neptune_on == 0,
				{neptune = Synth.new(\neptune, [
					\buf_l, bufferL,
					\buf_r, bufferR,
					\neptune, msg[1],
					\triton, msg[2],
					\nereid, msg[3],
					\pluto, msg[4],
					\orcus, msg[5],
					\ring, msg[6],
					\px, msg[7],
					\sun, msg[8],
					\gate, 1
				]); neptune_on = 1},
				{neptune.set(\gate, 0); neptune_on = 0}
			);
		});

		this.addCommand("neptunePrimary", "f", { arg msg;
			neptune.set(\neptune, msg[1])
		});

		this.addCommand("neptuneTriton", "f", { arg msg;
			neptune.set(\triton, msg[1])
		});

		this.addCommand("neptuneNereid", "f", { arg msg;
			neptune.set(\nereid, msg[1])
		});

		this.addCommand("neptunePluto", "f", { arg msg;
			neptune.set(\pluto, msg[1])
		});

		this.addCommand("neptuneOrcus", "f", { arg msg;
			neptune.set(\orcus, msg[1])
		});

		this.addCommand("neptuneRing", "f", { arg msg;
			neptune.set(\ring, msg[1])
		});

		//sun
		this.addCommand("sun", "f", { arg msg;
			mercury.set(\sun, msg[1]);
			venus.set(\sun, msg[1]);
			earth.set(\sun, msg[1]);
			mars.set(\sun, msg[1]);
			jupiter.set(\sun, msg[1]);
			saturn.set(\sun, msg[1]);
			uranus.set(\sun, msg[1]);
			neptune.set(\sun, msg[1])
		});


		//planet x

		this.addCommand("planetX", "f", { arg msg;
			mercury.set(\px, msg[1]);
			venus.set(\px, msg[1]);
			earth.set(\px, msg[1]);
			mars.set(\px, msg[1]);
			jupiter.set(\px, msg[1]);
			saturn.set(\px, msg[1]);
			uranus.set(\px, msg[1]);
			neptune.set(\px, msg[1])
		});


	}

	free {
		bufferL.free;
		bufferR.free;
		recSynth.free;

		mercury.free;
		venus.free;
		earth.free;
		mars.free;
		jupiter.free;
		saturn.free;
		uranus.free;
		neptune.free;

		mercuryEnvBuf.free;
		venusEnvBuf.free;
		earthEnvBuf.free;
		marsEnvBuf.free;
		jupiterEnvBuf.free;
		saturnEnvBuf.free;
		uranusEnvBuf.free;
	}
}