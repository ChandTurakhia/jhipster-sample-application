import * as dayjs from 'dayjs';
import { IPerson } from 'app/entities/person/person.model';
import { IAddressHeader } from 'app/entities/address-header/address-header.model';

export interface IPersonAddress {
  id?: number;
  addressTypeCode?: string | null;
  validFrom?: dayjs.Dayjs | null;
  validTo?: dayjs.Dayjs | null;
  people?: IPerson[] | null;
  addressHeader?: IAddressHeader | null;
}

export class PersonAddress implements IPersonAddress {
  constructor(
    public id?: number,
    public addressTypeCode?: string | null,
    public validFrom?: dayjs.Dayjs | null,
    public validTo?: dayjs.Dayjs | null,
    public people?: IPerson[] | null,
    public addressHeader?: IAddressHeader | null
  ) {}
}

export function getPersonAddressIdentifier(personAddress: IPersonAddress): number | undefined {
  return personAddress.id;
}
