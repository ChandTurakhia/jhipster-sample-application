import { IAddressHeader } from 'app/entities/address-header/address-header.model';

export interface ILocationHeader {
  id?: number;
  latitude?: number | null;
  longitude?: number | null;
  elevation?: number | null;
  addressHeader?: IAddressHeader | null;
}

export class LocationHeader implements ILocationHeader {
  constructor(
    public id?: number,
    public latitude?: number | null,
    public longitude?: number | null,
    public elevation?: number | null,
    public addressHeader?: IAddressHeader | null
  ) {}
}

export function getLocationHeaderIdentifier(locationHeader: ILocationHeader): number | undefined {
  return locationHeader.id;
}
