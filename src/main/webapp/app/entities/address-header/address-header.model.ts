import { IPersonAddress } from 'app/entities/person-address/person-address.model';
import { ILocationHeader } from 'app/entities/location-header/location-header.model';

export interface IAddressHeader {
  id?: number;
  typeCode?: string | null;
  standardized?: boolean | null;
  addressLine1?: string | null;
  addressLine2?: string | null;
  addressLine3?: string | null;
  cityName?: string | null;
  countyName?: string | null;
  stateCode?: string | null;
  zipCode?: string | null;
  countryName?: string | null;
  personAddress?: IPersonAddress | null;
  locationHeader?: ILocationHeader | null;
}

export class AddressHeader implements IAddressHeader {
  constructor(
    public id?: number,
    public typeCode?: string | null,
    public standardized?: boolean | null,
    public addressLine1?: string | null,
    public addressLine2?: string | null,
    public addressLine3?: string | null,
    public cityName?: string | null,
    public countyName?: string | null,
    public stateCode?: string | null,
    public zipCode?: string | null,
    public countryName?: string | null,
    public personAddress?: IPersonAddress | null,
    public locationHeader?: ILocationHeader | null
  ) {
    this.standardized = this.standardized ?? false;
  }
}

export function getAddressHeaderIdentifier(addressHeader: IAddressHeader): number | undefined {
  return addressHeader.id;
}
