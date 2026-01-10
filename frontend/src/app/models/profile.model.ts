export interface SocialLink {
  name: String;
  url: String;
  icon: String;
}

export interface Profile {
  name: string;
  title: string;
  summary: string;
  location: string;
  experienceYears: string;
  specialization: string;
  socialLinks: SocialLink[];
}
