/*
 * Copyright (c) Libly - Terl Tech Ltd  • 04/08/2019, 22:41 • libly.co, goterl.com
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v2.0. If a copy of the MPL was not distributed with this
 * file, you can obtain one at http://mozilla.org/MPL/2.0/.
 */

package co.libly.hydride;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RandomTest extends BaseTest {

    @Test
    public void getRandomUnsignedInt() {
        int randomByte = hydrogen.hydro_random_u32_get();
        assertTrue(randomByte >= 0);
    }

    @Test
    public void getRandomUnsignedIntWithUpperBound() {
        int upperBound = 20;
        int randomByte = hydrogen.hydro_random_uniform(20);
        assertTrue(randomByte < upperBound);
        assertTrue(randomByte >= 0);
    }

    @Test
    public void randomisedBuffer() {
        byte[] buffer = new byte[32];
        hydrogen.hydro_random_buf(buffer, buffer.length);
        assertTrue(hasAtLeastOneNonZeroNumber(buffer));
    }

    @Test
    public void randomisedBufferDeterministic() {
        byte[] buffer = new byte[32], buffer2 = new byte[32];
        byte[] seed = getSeed();

        hydrogen.hydro_random_buf_deterministic(buffer, buffer.length, seed);
        hydrogen.hydro_random_buf_deterministic(buffer2, buffer2.length, seed);

        assertTrue(arraysEqual(buffer, buffer2));
    }


}
