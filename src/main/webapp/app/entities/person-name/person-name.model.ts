import * as dayjs from 'dayjs';
import { IPerson } from 'app/entities/person/person.model';

export interface IPersonName {
  id?: number;
  personId?: number | null;
  firstName?: string | null;
  middleName?: string | null;
  lastName?: string | null;
  secondLastName?: string | null;
  preferredName?: string | null;
  prefixCode?: string | null;
  suffixCode?: string | null;
  validFrom?: dayjs.Dayjs | null;
  validTo?: dayjs.Dayjs | null;
  people?: IPerson[] | null;
}

export class PersonName implements IPersonName {
  constructor(
    public id?: number,
    public personId?: number | null,
    public firstName?: string | null,
    public middleName?: string | null,
    public lastName?: string | null,
    public secondLastName?: string | null,
    public preferredName?: string | null,
    public prefixCode?: string | null,
    public suffixCode?: string | null,
    public validFrom?: dayjs.Dayjs | null,
    public validTo?: dayjs.Dayjs | null,
    public people?: IPerson[] | null
  ) {}
}

export function getPersonNameIdentifier(personName: IPersonName): number | undefined {
  return personName.id;
}
