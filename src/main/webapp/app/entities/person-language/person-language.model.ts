import { IPerson } from 'app/entities/person/person.model';

export interface IPersonLanguage {
  id?: number;
  languageCode?: string | null;
  languageUsageCode?: string | null;
  preferredLanguage?: boolean | null;
  people?: IPerson[] | null;
}

export class PersonLanguage implements IPersonLanguage {
  constructor(
    public id?: number,
    public languageCode?: string | null,
    public languageUsageCode?: string | null,
    public preferredLanguage?: boolean | null,
    public people?: IPerson[] | null
  ) {
    this.preferredLanguage = this.preferredLanguage ?? false;
  }
}

export function getPersonLanguageIdentifier(personLanguage: IPersonLanguage): number | undefined {
  return personLanguage.id;
}
