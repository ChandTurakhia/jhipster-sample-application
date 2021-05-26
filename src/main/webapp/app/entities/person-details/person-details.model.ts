import { IPerson } from 'app/entities/person/person.model';

export interface IPersonDetails {
  id?: number;
  maritalTypeStatusCode?: string | null;
  raceEthinicityCode?: string | null;
  citizenshipStatusCode?: string | null;
  pregnant?: boolean | null;
  childrenCount?: number | null;
  height?: string | null;
  weight?: number | null;
  person?: IPerson | null;
}

export class PersonDetails implements IPersonDetails {
  constructor(
    public id?: number,
    public maritalTypeStatusCode?: string | null,
    public raceEthinicityCode?: string | null,
    public citizenshipStatusCode?: string | null,
    public pregnant?: boolean | null,
    public childrenCount?: number | null,
    public height?: string | null,
    public weight?: number | null,
    public person?: IPerson | null
  ) {
    this.pregnant = this.pregnant ?? false;
  }
}

export function getPersonDetailsIdentifier(personDetails: IPersonDetails): number | undefined {
  return personDetails.id;
}
