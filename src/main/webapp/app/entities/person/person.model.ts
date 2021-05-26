import * as dayjs from 'dayjs';
import { IPersonDetails } from 'app/entities/person-details/person-details.model';
import { IPersonName } from 'app/entities/person-name/person-name.model';
import { IPersonAddress } from 'app/entities/person-address/person-address.model';
import { IPersonLanguage } from 'app/entities/person-language/person-language.model';

export interface IPerson {
  id?: number;
  version?: number | null;
  stateCode?: string | null;
  dateOfBirth?: dayjs.Dayjs | null;
  dateOfDeath?: dayjs.Dayjs | null;
  genderTypeCode?: string | null;
  personDetails?: IPersonDetails | null;
  personName?: IPersonName | null;
  personAddress?: IPersonAddress | null;
  personLanguage?: IPersonLanguage | null;
}

export class Person implements IPerson {
  constructor(
    public id?: number,
    public version?: number | null,
    public stateCode?: string | null,
    public dateOfBirth?: dayjs.Dayjs | null,
    public dateOfDeath?: dayjs.Dayjs | null,
    public genderTypeCode?: string | null,
    public personDetails?: IPersonDetails | null,
    public personName?: IPersonName | null,
    public personAddress?: IPersonAddress | null,
    public personLanguage?: IPersonLanguage | null
  ) {}
}

export function getPersonIdentifier(person: IPerson): number | undefined {
  return person.id;
}
