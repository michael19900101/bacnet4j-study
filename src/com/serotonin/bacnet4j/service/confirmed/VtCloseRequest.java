/*
 * ============================================================================
 * GNU General Public License
 * ============================================================================
 *
 * Copyright (C) 2015 Infinite Automation Software. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * When signing a commercial license with Infinite Automation Software,
 * the following extension to GPL is made. A special exception to the GPL is
 * included to allow you to distribute a combined work that includes BAcnet4J
 * without being obliged to provide the source code for any proprietary components.
 *
 * See www.infiniteautomation.com for commercial license options.
 *
 * @author Matthew Lohbihler
 */
package com.serotonin.bacnet4j.service.confirmed;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.exception.NotImplementedException;
import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.bacnet4j.util.sero.ByteQueue;

public class VtCloseRequest extends ConfirmedRequestService {
    public static final byte TYPE_ID = 22;

    private final SequenceOf<UnsignedInteger> listOfRemoteVTSessionIdentifiers;

    public VtCloseRequest(final SequenceOf<UnsignedInteger> listOfRemoteVTSessionIdentifiers) {
        this.listOfRemoteVTSessionIdentifiers = listOfRemoteVTSessionIdentifiers;
    }

    @Override
    public byte getChoiceId() {
        return TYPE_ID;
    }

    @Override
    public void write(final ByteQueue queue) {
        write(queue, listOfRemoteVTSessionIdentifiers);
    }

    VtCloseRequest(final ByteQueue queue) throws BACnetException {
        listOfRemoteVTSessionIdentifiers = readSequenceOf(queue, UnsignedInteger.class);
    }

    @Override
    public AcknowledgementService handle(final LocalDevice localDevice, final Address from) throws BACnetException {
        throw new NotImplementedException();
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result
                + (listOfRemoteVTSessionIdentifiers == null ? 0 : listOfRemoteVTSessionIdentifiers.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final VtCloseRequest other = (VtCloseRequest) obj;
        if (listOfRemoteVTSessionIdentifiers == null) {
            if (other.listOfRemoteVTSessionIdentifiers != null)
                return false;
        } else if (!listOfRemoteVTSessionIdentifiers.equals(other.listOfRemoteVTSessionIdentifiers))
            return false;
        return true;
    }
}
