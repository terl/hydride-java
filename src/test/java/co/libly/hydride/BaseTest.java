/*
 * Copyright (c) Libly - Terl Tech Ltd  • 04/08/2019, 22:41 • libly.co, goterl.com
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v2.0. If a copy of the MPL was not distributed with this
 * file, you can obtain one at http://mozilla.org/MPL/2.0/.
 */

package co.libly.hydride;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseTest {

    public Hydrogen hydrogen;

    @BeforeAll
    public void doBeforeEverything() {
        hydrogen = new Hydrogen();
    }


    public byte[] getSeed() {
        return new byte[] {
                2, 4, 6, 8, 10, 12, 14, 16,
                18, 93, 9, 11, 19, 12, 14, 3,
                30, 4, 11, 8, 10, 15, 22, 14,
                20, 1, 3, 10, 32, 29, 5, 4
        };
    }

    public boolean hasAtLeastOneNonZeroNumber(byte[] buffer) {
        boolean hasAtLeastOneNonZeroNumber = false;
        for (int i = 0; i < buffer.length; i++) {
            if (buffer[i] != 0) {
                hasAtLeastOneNonZeroNumber = true;
                break;
            }
        }
        return hasAtLeastOneNonZeroNumber;
    }

    public boolean arraysEqual(byte[] buffer, byte[] buffer2) {
        return hydrogen.hydro_equal(buffer, buffer2, buffer.length);
    }

}
