const person = {
  name: "Byeon SungHoon",
  address: {
    line1: "Baker Street",
    city: "London",
    country: "UK",
  },
  profile: ["twitter", "linkedin", "instagram"],
  printProfile: () => {
    person.profile.map((profile) => console.log(profile));
  },
};

export default function LearningJavaScript() {
  return (
    <>
      <div>{person.name}</div>
      <div>{person.address.line1}</div>
      <div>{person.address.city}</div>
      <div>{person.profile[0]}</div>
      <div>{person.printProfile()}</div>
    </>
  );
}
